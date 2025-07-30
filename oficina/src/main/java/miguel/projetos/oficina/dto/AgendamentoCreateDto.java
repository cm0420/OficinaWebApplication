package miguel.projetos.oficina.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AgendamentoCreateDto {
    @NotNull(message = "A data e hora são obrigatórias.")
    @Future(message = "A data do agendamento deve ser no futuro.")
    private LocalDateTime data;

    @NotBlank(message = "O tipo de serviço é obrigatório.")
    private String tipo_servico;

    @NotBlank(message = "O ID do cliente é obrigatório.")
    private String id_cliente;

    @NotBlank(message = "O ID do carro é obrigatório.")
    private String id_carro;

    @NotBlank(message = "O ID do mecânico é obrigatório.")
    private String id_mecanico;
}
