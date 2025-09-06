package miguel.projetos.oficina.dto;

import lombok.Data;

@Data
public class ResumoPontoDto {
    private String cpf;
    private int ano;
    private int mes;
    private long diasTrabalhados;
    private long horasTrabalhadas;
    private long horasExtras;
}
