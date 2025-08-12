package miguel.projetos.oficina.entity;

import org.springframework.data.domain.Persistable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "carros")
@Data
@NoArgsConstructor
public class Carro implements Persistable<String> {

    @Id
    private String id_carro;

    @Column(unique = true, nullable = false)
    private String chassi;

    @Column(unique = true, nullable = false)
    private String placa;

    private String fabricante;
    private String modelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cpf_dono", referencedColumnName = "cpf", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Cliente dono;

    /** IMPORTANTÍSSIMO: indica ao Spring Data se é novo (INSERT) ou não (UPDATE) */
    @Transient
    private boolean _isNew = true;

    @Override
    public String getId() { return this.id_carro; }

    @Override
    public boolean isNew() { return this._isNew; }

    /** Quando a JPA carregar/salvar, marca como não-novo */
    @PostLoad
    @PostPersist
    private void markNotNew() { this._isNew = false; }

    /** Útil se quiser forçar novo manualmente em algum fluxo específico */
    public void markNew() { this._isNew = true; }
}
