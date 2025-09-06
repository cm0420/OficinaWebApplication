package miguel.projetos.oficina.dto;

import lombok.Data;

@Data
public class FuncionarioPublicDto {
    private String id_usuario;
    private String nome;
    private String cargo;
    private String telefone; // pode ser mascarado se quiser
}
