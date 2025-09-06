package miguel.projetos.oficina.service;

import miguel.projetos.oficina.Repository.AgendamentoRepository;
import miguel.projetos.oficina.Repository.OrdemDeServicoRepository;
import miguel.projetos.oficina.Repository.PecaRepository;
import miguel.projetos.oficina.Repository.PecaUtilizadaRepository;
import miguel.projetos.oficina.entity.Agendamento;
import miguel.projetos.oficina.entity.OrdemDeServico;
import miguel.projetos.oficina.entity.Peca;
import miguel.projetos.oficina.entity.PecaUtilizada;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    @Autowired
    private IdGeneratorService idGeneratorService;

    @Value("${oficina.negocio.custo.mao-de-obra}") // Injeta o custo da mão de obra
    private BigDecimal custoMaoDeObra;

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

        Long proximoId = idGeneratorService.getNextId("ordem_de_servico");
        String numeroOsFormatado = "Ordem-Serviço" + String.format("%03d", proximoId);
        novaOS.setNumero_os(numeroOsFormatado);


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

        BigDecimal valorTotal = calcularValorTotal(os);

        // Chama o serviço financeiro para registrar a receita e a comissão
        financeiroService.registrarFaturamentoOS(os, valorTotal);

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
    public BigDecimal calcularValorTotal(OrdemDeServico os) {
        // Soma o subtotal de todas as peças
        BigDecimal totalPecas = os.getPecasUtilizadas().stream()
                .map(peca -> peca.getPreco_no_momento_do_uso().multiply(new BigDecimal(peca.getQuantidade_utilizada())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Adiciona o custo da mão de obra
        return totalPecas.add(custoMaoDeObra);
    }
}
