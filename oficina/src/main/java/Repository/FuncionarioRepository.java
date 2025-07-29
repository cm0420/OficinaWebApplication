package Repository;

import entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, String> {
    Optional<Funcionario> findFuncionarioByCpf(String cpf);

    Optional<Funcionario> deleteFuncionarioByCpf(String cpf);
}
