package miguel.projetos.oficina.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrdemDeServicoCreateDto {
    @NotNull(message = "O ID do agendamento é obrigatório.")
    private Long agendamentoId;

    @NotBlank(message = "O defeito relatado é obrigatório.")
    private String defeitoRelatado;
}
