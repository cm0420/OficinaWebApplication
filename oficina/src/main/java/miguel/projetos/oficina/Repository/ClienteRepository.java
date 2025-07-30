package miguel.projetos.oficina.Repository;

import miguel.projetos.oficina.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {
    Optional<Cliente> findByCpf(String cpf);
    Optional<Cliente> deleteClienteByCpf(String cpf);

}
