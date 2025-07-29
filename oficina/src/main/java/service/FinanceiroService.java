package service;

import Repository.FuncionarioRepository;
import Repository.RegistroFinanceiroRepository;
import entity.Cliente;
import entity.Funcionario;
import entity.OrdemDeServico;
import entity.RegistroFinanceiro;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FinanceiroService {
    @Autowired
    RegistroFinanceiroRepository registroFinanceiroRepository;
    @Autowired
    FuncionarioRepository funcionarioRepository;
    @Transactional
    public void registrarTaxaCancelamento(Cliente cliente, Double valorTaxa, String motivo) {
        RegistroFinanceiro registro = new RegistroFinanceiro();
        registro.setTipo("RECEITA_CANCELAMENTO");
        registro.setDescricao("Taxa de cancelamento para cliente " + cliente.getNome() + ". Motivo: " + motivo);
        registro.setValor(new BigDecimal(valorTaxa));
        registro.setData(LocalDateTime.now());
        registroFinanceiroRepository.save(registro);
    }
    @Transactional
    public void registrarFaturamentoOS(OrdemDeServico os) {
        // 1. Registra a receita total da OS
        RegistroFinanceiro receita = new RegistroFinanceiro();
        receita.setTipo("RECEITA_SERVICO");
        BigDecimal valorTotal = os.calcularValorTotal(); // Este método precisa ser criado na OS
        receita.setValor(valorTotal);
        receita.setDescricao("Receita da OS #" + os.getNumero_os() + " para cliente " + os.getCliente().getNome());
        receita.setData(LocalDateTime.now());
        registroFinanceiroRepository.save(receita);

        // 2. Registra a despesa da comissão do mecânico (ex: 5%)
        RegistroFinanceiro comissao = new RegistroFinanceiro();
        comissao.setTipo("DESPESA_COMISSAO");
        BigDecimal valorComissao = valorTotal.multiply(new BigDecimal("0.05"));
        comissao.setValor(valorComissao);
        comissao.setDescricao("Comissão (5%) da OS #" + os.getNumero_os() + " para mecânico " + os.getMecanico().getNome());
        comissao.setData(LocalDateTime.now());
        registroFinanceiroRepository.save(comissao);
    }
    @Transactional
    public void registrarDespesaPecas(String descricao, BigDecimal valorTotal) {
        RegistroFinanceiro registro = new RegistroFinanceiro();
        registro.setTipo("DESPESA_PECAS");
        registro.setDescricao(descricao);
        registro.setValor(valorTotal);
        registro.setData(LocalDateTime.now());
        registroFinanceiroRepository.save(registro);
    }
    @Transactional
    public void pagarSalarios() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        for (Funcionario f : funcionarios) {
            BigDecimal salario = BigDecimal.ZERO;
            if ("Atendente".equals(f.getCargo())) salario = new BigDecimal("1000.00");
            if ("Mecanico".equals(f.getCargo())) salario = new BigDecimal("1500.00");
            // Adicionar outros cargos se necessário

            if (salario.compareTo(BigDecimal.ZERO) > 0) {
                RegistroFinanceiro registro = new RegistroFinanceiro();
                registro.setTipo("DESPESA_SALARIO");
                registro.setDescricao("Salário de " + f.getNome());
                registro.setValor(salario);
                registro.setData(LocalDateTime.now());
                registroFinanceiroRepository.save(registro);
            }
        }
    }
}
