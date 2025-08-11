package miguel.projetos.oficina.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PontoRequestDto {
    @NotBlank(message = "O CPF do funcionário é obrigatório.")
    private String cpf;
}
