package miguel.projetos.oficina.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PecaUtilizadaDto {
    private Long id;
    private String nomePeca;
    private Integer quantidade_utilizada;
    private BigDecimal preco_no_momento_do_uso;
    private BigDecimal subtotal;
}
