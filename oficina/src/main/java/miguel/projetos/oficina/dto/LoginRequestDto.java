package miguel.projetos.oficina.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank(message = "O CPF é obrigatório.")
    private String cpf;

    @NotBlank(message = "A senha é obrigatória.")
    private String senha;
}
