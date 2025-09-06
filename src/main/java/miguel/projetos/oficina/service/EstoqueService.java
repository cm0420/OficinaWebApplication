package miguel.projetos.oficina.service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import miguel.projetos.oficina.Repository.PecaRepository;
import miguel.projetos.oficina.entity.Peca;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {
    @Autowired
    private PecaRepository pecaRepository;

    @Autowired
    private FinanceiroService financeiroService;
    @Autowired
    private IdGeneratorService idGeneratorService;

    public List<Peca> findAll(){
        return pecaRepository.findAll();
    }

    public Optional<Peca> findById(String id) {
        return pecaRepository.findById(id);
    }

    @Transactional
    public Peca cadastrarNovaPeca(Peca peca) {
        BigDecimal custoTotal = peca.getPreco()
                .multiply(new BigDecimal("0.7"))
                .multiply(new BigDecimal(peca.getQuantidade()));

        String descricao = "Compra inicial de " + peca.getQuantidade() + "x " + peca.getNome();
        financeiroService.registrarDespesaPecas(descricao, custoTotal);

        Long proximoId = idGeneratorService.getNextId("peca");
        String idFormatado = "PR-" + String.format("%03d", proximoId);
        peca.setId_produto(idFormatado);

        return pecaRepository.save(peca);
    }
    @Transactional
    public Peca update(String id, Peca pecaAtualizada) {
        Peca pecaExistente = pecaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Peça com ID " + id + " não encontrada."));

        pecaExistente.setNome(pecaAtualizada.getNome());
        pecaExistente.setPreco(pecaAtualizada.getPreco());
        pecaExistente.setFornecedor(pecaAtualizada.getFornecedor());
        // A quantidade não é alterada aqui, mas sim no método de reporEstoque

        return pecaRepository.save(pecaExistente);
    }
    @Transactional
    public Peca reporEstoque(String pecaId, int quantidadeAdicional) {
        Peca peca = pecaRepository.findById(pecaId)
                .orElseThrow(() -> new IllegalArgumentException("Peça com ID " + pecaId + " não encontrada."));

        peca.setQuantidade(peca.getQuantidade() + quantidadeAdicional);

        BigDecimal custoTotal = peca.getPreco()
                .multiply(new BigDecimal("0.7"))
                .multiply(new BigDecimal(quantidadeAdicional));

        String descricao = "Reposição de " + quantidadeAdicional + "x " + peca.getNome();
        financeiroService.registrarDespesaPecas(descricao, custoTotal);

        return pecaRepository.save(peca);
    }

}
