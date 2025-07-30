package miguel.projetos.oficina.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="registros_financeiros")
@Data
@NoArgsConstructor
public class RegistroFinanceiro {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    private String tipo;
    @Column(nullable = false)
    private LocalDateTime data;
}
