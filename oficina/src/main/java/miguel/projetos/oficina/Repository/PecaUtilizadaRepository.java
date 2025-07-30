package miguel.projetos.oficina.Repository;

import miguel.projetos.oficina.entity.PecaUtilizada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PecaUtilizadaRepository extends JpaRepository<PecaUtilizada, Long> {
}
