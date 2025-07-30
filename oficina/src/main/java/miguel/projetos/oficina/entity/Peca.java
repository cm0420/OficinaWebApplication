package miguel.projetos.oficina.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "pecas")
@Data
@NoArgsConstructor
public class Peca {
    @Id
    private String id_produto;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;
    @Column(nullable = false)
    private Integer quantidade;
    private String fornecedor;

}
