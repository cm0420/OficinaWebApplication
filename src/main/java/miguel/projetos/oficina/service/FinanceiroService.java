package miguel.projetos.oficina.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import miguel.projetos.oficina.Repository.FuncionarioRepository;
import miguel.projetos.oficina.Repository.RegistroFinanceiroRepository;
import miguel.projetos.oficina.dto.HoleriteDto;
import miguel.projetos.oficina.dto.RelatorioMensalDto;
import miguel.projetos.oficina.dto.ResumoPontoDto;
import miguel.projetos.oficina.entity.Cliente;
import miguel.projetos.oficina.entity.Funcionario;
import miguel.projetos.oficina.entity.OrdemDeServico;
import miguel.projetos.oficina.entity.RegistroFinanceiro;

@Service
public class FinanceiroService {

    @Autowired
    private RegistroFinanceiroRepository registroFinanceiroRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PontoService pontoService;

    // valores configuráveis no application.properties
    @Value("${oficina.negocio.salario.atendente}")
    private BigDecimal salarioAtendente;

    @Value("${oficina.negocio.salario.mecanico}")
    private BigDecimal salarioMecanico;

    @Value("${oficina.negocio.comissao.percentual}")
    private BigDecimal percentualComissao;

    @Value("${oficina.negocio.taxa-cancelamento.valor}")
    private BigDecimal taxaCancelamento;

    // ========= MÉTODOS EXISTENTES =========

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
        registro.setValor(taxaCancelamento);
        registro.setData(LocalDateTime.now());
        registroFinanceiroRepository.save(registro);
    }

    @Transactional
    public void registrarFaturamentoOS(OrdemDeServico os, BigDecimal valorTotal) {
        // Receita total da OS
        RegistroFinanceiro receita = new RegistroFinanceiro();
        receita.setTipo("RECEITA_SERVICO");
        receita.setValor(valorTotal);
        receita.setDescricao("Receita da OS #" + os.getNumero_os() + " para cliente " + os.getCliente().getNome());
        receita.setData(LocalDateTime.now());
        registroFinanceiroRepository.save(receita);

        // Despesa da comissão do mecânico
        RegistroFinanceiro comissao = new RegistroFinanceiro();
        comissao.setTipo("DESPESA_COMISSAO");
        BigDecimal valorComissao = valorTotal.multiply(percentualComissao);
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
            BigDecimal salario = getSalarioBase(f);

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

    // ========= NOVOS MÉTODOS =========

    /**
     * Relatório mensal consolidado da folha
     */
    public RelatorioMensalDto getRelatorioMensal(int ano, int mes) {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();

        List<ResumoPontoDto> resumos = funcionarios.stream()
                .map(f -> pontoService.getResumoMensal(f.getCpf(), ano, mes))
                .collect(Collectors.toList());

        long totalDiasTrabalhados = resumos.stream().mapToLong(ResumoPontoDto::getDiasTrabalhados).sum();
        long totalHoras = resumos.stream().mapToLong(ResumoPontoDto::getHorasTrabalhadas).sum();
        long totalExtras = resumos.stream().mapToLong(ResumoPontoDto::getHorasExtras).sum();

        BigDecimal custoSalarios = funcionarios.stream()
                .map(this::getSalarioBase)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Exemplo de regra simples → depois pode refinar
        BigDecimal custoExtras = BigDecimal.valueOf(totalExtras * 10); // R$10/h extra
        BigDecimal custoFinal = custoSalarios.add(custoExtras);

        RelatorioMensalDto dto = new RelatorioMensalDto();
        dto.setAno(ano);
        dto.setMes(mes);
        dto.setFuncionarios(resumos);
        dto.setTotalDiasTrabalhados(totalDiasTrabalhados);
        dto.setTotalHorasTrabalhadas(totalHoras);
        dto.setTotalHorasExtras(totalExtras);
        dto.setCustoTotalSalarios(custoSalarios);
        dto.setCustoHorasExtras(custoExtras);
        dto.setCustoFinalFolha(custoFinal);

        return dto;
    }

    /**
     * Holerite de um funcionário no mês
     */
    public HoleriteDto getHolerite(String funcionarioId, int ano, int mes) {
        Funcionario f = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new NoSuchElementException("Funcionário não encontrado."));

        ResumoPontoDto resumo = pontoService.getResumoMensal(f.getCpf(), ano, mes);

        BigDecimal salarioBase = getSalarioBase(f);
        BigDecimal valorHorasExtras = BigDecimal.valueOf(resumo.getHorasExtras() * 10); // regra de exemplo
        BigDecimal salarioLiquido = salarioBase.add(valorHorasExtras);

        HoleriteDto dto = new HoleriteDto();
        dto.setFuncionario(f.getNome());
        dto.setCpf(f.getCpf());
        dto.setAno(ano);
        dto.setMes(mes);
        dto.setSalarioBase(salarioBase);
        dto.setHorasExtras(resumo.getHorasExtras());
        dto.setValorHorasExtras(valorHorasExtras);
        dto.setDiasTrabalhados(resumo.getDiasTrabalhados());
        dto.setHorasTrabalhadas(resumo.getHorasTrabalhadas());
        dto.setSalarioLiquido(salarioLiquido);

        return dto;
    }

    // ========= HELPER =========

    private BigDecimal getSalarioBase(Funcionario f) {
        if ("Atendente".equalsIgnoreCase(f.getCargo())) return salarioAtendente;
        if ("Mecanico".equalsIgnoreCase(f.getCargo())) return salarioMecanico;
        return BigDecimal.ZERO;
    }
}
