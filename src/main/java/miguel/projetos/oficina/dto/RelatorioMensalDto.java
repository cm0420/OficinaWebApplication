package miguel.projetos.oficina.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class RelatorioMensalDto {
    private int ano;
    private int mes;

    // lista com o resumo de cada funcionário
    private List<ResumoPontoDto> funcionarios;

    // totais do mês
    private long totalDiasTrabalhados;
    private long totalHorasTrabalhadas;
    private long totalHorasExtras;

    // totais financeiros
    private BigDecimal custoTotalSalarios;   // soma dos salários base
    private BigDecimal custoHorasExtras;     // custo adicional de horas extras
    private BigDecimal custoFinalFolha;      // custo líquido (salários + extras)
}

