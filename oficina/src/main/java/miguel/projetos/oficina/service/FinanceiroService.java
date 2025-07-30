package miguel.projetos.oficina.service;

import miguel.projetos.oficina.Repository.FuncionarioRepository;
import miguel.projetos.oficina.Repository.RegistroFinanceiroRepository;
import miguel.projetos.oficina.entity.Cliente;
import miguel.projetos.oficina.entity.Funcionario;
import miguel.projetos.oficina.entity.OrdemDeServico;
import miguel.projetos.oficina.entity.RegistroFinanceiro;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinanceiroService {
    @Autowired
    private RegistroFinanceiroRepository registroFinanceiroRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    // Injetando os valores configurados do application.properties
    @Value("${oficina.negocio.salario.atendente}")
    private BigDecimal salarioAtendente;

    @Value("${oficina.negocio.salario.mecanico}")
    private BigDecimal salarioMecanico;

    @Value("${oficina.negocio.comissao.percentual}")
    private BigDecimal percentualComissao;

    @Value("${oficina.negocio.taxa-cancelamento.valor}")
    private BigDecimal taxaCancelamento;

    public List<RegistroFinanceiro> findAllRegistros() {
        return registroFinanceiroRepository.findAll();
    }

    public Map<String, BigDecimal> getBalancoFinanceiro() {
        List<RegistroFinanceiro> registros = registroFinanceiroRepository.findAll();

        BigDecimal totalReceitas = BigDecimal.ZERO;
        BigDecimal totalDespesas = BigDecimal.ZERO;

        for (RegistroFinanceiro registro : registros) {
            if (registro.getTipo().startsWith("RECEITA")) {
                totalReceitas = totalReceitas.add(registro.getValor());
            } else if (registro.getTipo().startsWith("DESPESA")) {
                totalDespesas = totalDespesas.add(registro.getValor());
            }
        }

        BigDecimal lucro = totalReceitas.subtract(totalDespesas);

        Map<String, BigDecimal> balanco = new HashMap<>();
        balanco.put("totalReceitas", totalReceitas);
        balanco.put("totalDespesas", totalDespesas);
        balanco.put("lucro", lucro);

        return balanco;
    }
    @Transactional
    public void registrarTaxaCancelamento(Cliente cliente, String motivo) {
        RegistroFinanceiro registro = new RegistroFinanceiro();
        registro.setTipo("RECEITA_CANCELAMENTO");
        registro.setDescricao("Taxa de cancelamento para cliente " + cliente.getNome() + ". Motivo: " + motivo);
        registro.setValor(taxaCancelamento); // <-- USA A VARIÁVEL CONFIGURADA
        registro.setData(LocalDateTime.now());
        registroFinanceiroRepository.save(registro);
    }

    @Transactional
    public void registrarFaturamentoOS(OrdemDeServico os, BigDecimal valorTotal) {
        // 1. Registra a receita total da OS
        RegistroFinanceiro receita = new RegistroFinanceiro();
        receita.setTipo("RECEITA_SERVICO");
        receita.setValor(valorTotal);
        receita.setDescricao("Receita da OS #" + os.getNumero_os() + " para cliente " + os.getCliente().getNome());
        receita.setData(LocalDateTime.now());
        registroFinanceiroRepository.save(receita);

        // 2. Registra a despesa da comissão do mecânico
        RegistroFinanceiro comissao = new RegistroFinanceiro();
        comissao.setTipo("DESPESA_COMISSAO");
        BigDecimal valorComissao = valorTotal.multiply(percentualComissao); // <-- USA A VARIÁVEL CONFIGURADA
        comissao.setValor(valorComissao);
        comissao.setDescricao("Comissão (" + (percentualComissao.doubleValue() * 100) + "%) da OS #" + os.getNumero_os() + " para mecânico " + os.getMecanico().getNome());
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
            if ("Atendente".equals(f.getCargo())) salario = salarioAtendente; // <-- USA A VARIÁVEL CONFIGURADA
            if ("Mecanico".equals(f.getCargo())) salario = salarioMecanico; // <-- USA A VARIÁVEL CONFIGURADA

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
