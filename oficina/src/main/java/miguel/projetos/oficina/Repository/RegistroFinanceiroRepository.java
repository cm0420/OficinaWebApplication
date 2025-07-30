package miguel.projetos.oficina.Repository;

import miguel.projetos.oficina.entity.RegistroFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroFinanceiroRepository extends JpaRepository<RegistroFinanceiro, Long> {
}
