package miguel.projetos.oficina.Repository;

import miguel.projetos.oficina.entity.Peca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PecaRepository extends JpaRepository<Peca, String> {
}
