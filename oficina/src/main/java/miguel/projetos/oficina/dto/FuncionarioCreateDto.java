package miguel.projetos.oficina.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FuncionarioCreateDto {
    @NotBlank(message = "O CPF é obrigatório.")
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos.")
    private String cpf;

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 4, message = "A senha deve ter no mínimo 4 caracteres.")
    private String senha;

    @NotBlank(message = "O cargo é obrigatório.")
    private String cargo;

    private String telefone;
    private String endereco;
    private String email;
}
