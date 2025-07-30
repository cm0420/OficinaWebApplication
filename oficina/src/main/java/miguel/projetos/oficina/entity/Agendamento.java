package miguel.projetos.oficina.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name= "agendamentos")
@Data
@NoArgsConstructor
public class Agendamento {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime data_hora;

    private String tipo_sevico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Cliente cliente;

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

}
