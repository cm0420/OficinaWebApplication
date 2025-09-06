package miguel.projetos.oficina.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carro", nullable = false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Carro carro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mecanico", nullable = false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Funcionario mecanico;

    @OneToMany(mappedBy = "ordemDeServico",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private List<PecaUtilizada> pecasUtilizadas = new ArrayList<>(); // ← nunca nula

    // Getter defensivo (sobrepõe o do Lombok)
    public List<PecaUtilizada> getPecasUtilizadas() {
        if (pecasUtilizadas == null) {
            pecasUtilizadas = new ArrayList<>();
        }
        return pecasUtilizadas;
    }

    // Conveniências para manter os dois lados do relacionamento em ordem
    public void addPeca(PecaUtilizada p) {
        getPecasUtilizadas().add(p);
        p.setOrdemDeServico(this);
    }

    public void removePeca(PecaUtilizada p) {
        getPecasUtilizadas().remove(p);
        p.setOrdemDeServico(null);
    }
}
