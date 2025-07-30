package miguel.projetos.oficina.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
public class Cliente {
    @Id
    private String id_cliente;
    @Column(unique = true, nullable = false, length = 11)
    private String cpf;
    @Column(nullable = false)
    private String nome;

    private String telefone;
    private String endereco;
    private String email;

    @OneToMany(mappedBy = "dono")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Carro> carros;
}
