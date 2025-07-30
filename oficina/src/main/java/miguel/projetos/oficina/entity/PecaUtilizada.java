package miguel.projetos.oficina.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.math.BigDecimal;

@Entity
@Table(name = "pecas_utilizadas")
@Data
@NoArgsConstructor
public class PecaUtilizada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numero_os", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private OrdemDeServico ordemDeServico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produto", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Peca peca;

    @Column(nullable = false)
    private Integer quantidade_utilizada;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco_no_momento_do_uso;
}
