package entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name= "registros_ponto")
@Data
@NoArgsConstructor
public class RegistroPonto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_funcionario", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Funcionario funcionario;

    @Column(nullable = false)
    private LocalDateTime data_hora_entrada;

    private LocalDate data_hora_salida;
}
