package miguel.projetos.oficina.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import miguel.projetos.oficina.entity.Carro;

@Repository
public interface CarroRepository extends JpaRepository<Carro, String> {
    // já usava:
    List<Carro> findCarroByDonoCpf(String cpfDono);

    // necessários pelo service:
    boolean existsByPlacaIgnoreCase(String placa);
    boolean existsByChassi(String chassi);
}
