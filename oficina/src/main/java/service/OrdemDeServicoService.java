package service;

import Repository.AgendamentoRepository;
import Repository.OrdemDeServicoRepository;
import Repository.PecaRepository;
import Repository.PecaUtilizadaRepository;
import entity.Agendamento;
import entity.OrdemDeServico;
import entity.Peca;
import entity.PecaUtilizada;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrdemDeServicoService {
    @Autowired
    OrdemDeServicoRepository ordemDeServicoRepository;

    @Autowired
    AgendamentoRepository agendamentoRepository;
    @Autowired
    PecaRepository pecaRepository;
    @Autowired
    PecaUtilizadaRepository pecaUtilizadaRepository;
    @Autowired
    FinanceiroService financeiroService;

    public List<OrdemDeServico> findAll() {
        return ordemDeServicoRepository.findAll();
    }
    public Optional<OrdemDeServico> findById(String id) {
        return ordemDeServicoRepository.findById(id);
    }

    @Transactional
    public OrdemDeServico abrirOS(Long agendamentoId, String defeitoRelatado) {
        // 1. Busca o agendamento
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento com ID " + agendamentoId + " não encontrado."));

        // 2. Cria a nova Ordem de Serviço com os dados do agendamento
        OrdemDeServico novaOS = new OrdemDeServico();
        novaOS.setNumero_os("OS-" + UUID.randomUUID().toString().substring(0, 8)); // Gera um ID único
        novaOS.setCliente(agendamento.getCliente());
        novaOS.setCarro(agendamento.getCarro());
        novaOS.setMecanico(agendamento.getMecanico());
        novaOS.setDefeito_relatado(defeitoRelatado);
        novaOS.setData_abertura(LocalDateTime.now());
        novaOS.setStatus("AGUARDANDO"); // Estado inicial

        // 3. Salva a nova OS no banco
        OrdemDeServico osSalva = ordemDeServicoRepository.save(novaOS);

        // 4. Remove o agendamento original
        agendamentoRepository.deleteById(agendamentoId);

        return osSalva;
    }
    @Transactional
    public OrdemDeServico adicionarPeca(String osId, String pecaId, int quantidade) {
        OrdemDeServico os = ordemDeServicoRepository.findById(osId)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço com ID " + osId + " não encontrada."));

        Peca peca = pecaRepository.findById(pecaId)
                .orElseThrow(() -> new IllegalArgumentException("Peça com ID " + pecaId + " não encontrada."));

        // Regra de Negócio: Verifica se o status permite adicionar peças
        if (!"EM_SERVICO".equals(os.getStatus())) {
            throw new IllegalStateException("Só é possível adicionar peças em Ordens de Serviço com status 'EM_SERVIÇO'.");
        }

        // Regra de Negócio: Verifica se há estoque suficiente
        if (peca.getQuantidade() < quantidade) {
            throw new IllegalStateException("Estoque insuficiente para a peça '" + peca.getNome() + "'.");
        }

        // Atualiza a quantidade no estoque
        peca.setQuantidade(peca.getQuantidade() - quantidade);
        pecaRepository.save(peca);

        // Cria o registro da peça utilizada
        PecaUtilizada pecaUtilizada = new PecaUtilizada();
        pecaUtilizada.setOrdemDeServico(os);
        pecaUtilizada.setPeca(peca);
        pecaUtilizada.setQuantidade_utilizada(quantidade);
        pecaUtilizada.setPreco_no_momento_do_uso(peca.getPreco()); // "Congela" o preço
        pecaUtilizadaRepository.save(pecaUtilizada);

        return ordemDeServicoRepository.findById(osId).get(); // Retorna a OS atualizada
    }
    @Transactional
    public OrdemDeServico iniciarInspecao(String osId) {
        OrdemDeServico os = ordemDeServicoRepository.findById(osId)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço não encontrada."));
        os.setStatus("EM_INSPECAO");
        return ordemDeServicoRepository.save(os);
    }
    @Transactional
    public OrdemDeServico iniciarServico(String osId) {
        OrdemDeServico os = ordemDeServicoRepository.findById(osId)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço não encontrada."));
        os.setStatus("EM_SERVICO");
        return ordemDeServicoRepository.save(os);
    }
    public OrdemDeServico finalizarServico(String osId) {
        OrdemDeServico os = ordemDeServicoRepository.findById(osId)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço não encontrada."));

        os.setStatus("FINALIZADA");
        os.setData_fechamento(LocalDateTime.now());

        // Chama o serviço financeiro para registrar a receita e a comissão
        financeiroService.registrarFaturamentoOS(os);

        return ordemDeServicoRepository.save(os);
    }
    public OrdemDeServico cancelarOS(String osId) {
        OrdemDeServico os = ordemDeServicoRepository.findById(osId)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço não encontrada."));

        if("FINALIZADA".equals(os.getStatus())){
            throw new IllegalStateException("Não é possível cancelar uma Ordem de Serviço já finalizada.");
        }

        os.setStatus("CANCELADA");
        return ordemDeServicoRepository.save(os);
    }
}
