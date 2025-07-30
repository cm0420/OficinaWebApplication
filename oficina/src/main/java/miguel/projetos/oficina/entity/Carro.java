package miguel.projetos.oficina.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "carros")
@Data
@NoArgsConstructor
public class Carro {

    @Id
    private String id_carro;

    @Column(unique = true, nullable = false)
    private String chassi;

    @Column(nullable = false)
    private String placa;

    private String fabricante;
    private String modelo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cpf_dono", referencedColumnName = "cpf", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Cliente dono;
}
