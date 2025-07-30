package miguel.projetos.oficina.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RegistroFinanceiroDto {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private String tipo; // ex: RECEITA_SERVICO, DESPESA_PECAS
    private LocalDateTime data;
}
