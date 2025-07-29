package Repository;

import entity.Funcionario;
import entity.OrdemDeServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdemDeServicoRepository extends JpaRepository<OrdemDeServico, String> {
    List<OrdemDeServico> findByMecanico(Funcionario mecanico);
}
