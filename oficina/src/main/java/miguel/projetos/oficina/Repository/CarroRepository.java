package miguel.projetos.oficina.Repository;

import miguel.projetos.oficina.entity.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarroRepository  extends JpaRepository<Carro, String> {
    List<Carro> findCarroByDonoCpf(String cpfDono);
}
