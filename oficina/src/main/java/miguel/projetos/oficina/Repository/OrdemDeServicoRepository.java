package miguel.projetos.oficina.Repository;

import miguel.projetos.oficina.entity.Carro;
import miguel.projetos.oficina.entity.Cliente;
import miguel.projetos.oficina.entity.Funcionario;
import miguel.projetos.oficina.entity.OrdemDeServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdemDeServicoRepository extends JpaRepository<OrdemDeServico, String> {
    List<OrdemDeServico> findByMecanico(Funcionario mecanico);
    List<OrdemDeServico> findByCliente(Cliente cliente);
    List<OrdemDeServico> findByCarro(Carro carro);
}
