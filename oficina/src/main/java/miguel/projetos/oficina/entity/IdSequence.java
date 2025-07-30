package miguel.projetos.oficina.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "id_sequences")
@Data
public class IdSequence {
    @Id
    private String entity_name;
    private Long last_id;
}
