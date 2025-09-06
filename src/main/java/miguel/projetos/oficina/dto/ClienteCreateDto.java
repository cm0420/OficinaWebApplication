package miguel.projetos.oficina.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteCreateDto {

    @NotBlank(message = "O CPF é obrigatório.")
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos.")
    private String cpf;

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    private String telefone;
    private String endereco;

    @Email(message = "Formato de e-mail inválido.")
    private String email;
}
