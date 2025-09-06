package miguel.projetos.oficina.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PontoDto {
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private long horasTrabalhadas; // em horas
}
