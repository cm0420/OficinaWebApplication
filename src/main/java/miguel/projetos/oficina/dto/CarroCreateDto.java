package miguel.projetos.oficina.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CarroCreateDto {

    @NotBlank
    private String chassi;
    @NotBlank
    private String placa;
    private String fabricante;
    private String modelo;

    @NotBlank(message = "O CPF do dono é obrigatório.")
    private String cpfDono;
}
