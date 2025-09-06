package miguel.projetos.oficina.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class HoleriteDto {
    private String funcionario;
    private String cpf;
    private int ano;
    private int mes;

    private BigDecimal salarioBase;
    private long diasTrabalhados;
    private long horasTrabalhadas;
    private long horasExtras;
    private BigDecimal valorHorasExtras;
    private BigDecimal salarioLiquido;
}
