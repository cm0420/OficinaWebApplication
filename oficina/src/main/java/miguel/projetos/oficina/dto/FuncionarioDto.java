package miguel.projetos.oficina.dto;

import lombok.Data;

@Data
public class FuncionarioDto {
    private String id_usuario;
    private String cpf;
    private String nome;
    private String cargo;
    private String telefone;
    private String endereco;
    private String email;
}
