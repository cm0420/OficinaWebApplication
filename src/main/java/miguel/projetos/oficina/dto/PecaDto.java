package miguel.projetos.oficina.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class PecaDto {
    private String id_produto;

    @NotBlank(message = "O nome da peça é obrigatório.")
    private String nome;

    @NotNull(message = "O preço é obrigatório.")
    @Positive(message = "O preço deve ser um valor positivo.")
    private BigDecimal preco;

    @NotNull(message = "A quantidade é obrigatória.")
    @PositiveOrZero(message = "A quantidade não pode ser negativa.")
    private Integer quantidade;

    private String fornecedor;
}
