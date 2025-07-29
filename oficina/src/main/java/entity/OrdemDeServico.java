package entity;
import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "ordens_de_servico")
@Data
@NoArgsConstructor
public class OrdemDeServico {
    @Id
    private String numero_os;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String defeito_relatado;

    @Column(nullable = false)
    private LocalDateTime data_abertura;

    private LocalDateTime data_fechamento;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carro", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Carro carro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mecanico", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Funcionario mecanico;

    @OneToMany(mappedBy = "ordemDeServico", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<PecaUtilizada> pecasUtilizadas;
}
