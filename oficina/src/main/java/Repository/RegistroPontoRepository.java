package Repository;

import entity.Funcionario;
import entity.RegistroPonto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroPontoRepository extends CrudRepository<RegistroPonto, Long> {
    List<RegistroPonto> findByFuncionario(Funcionario funcionario);
    Optional<RegistroPonto> findFirstByFuncionarioAndData_hora_saidaIsNullOrderByData_hora_entradaDesc(Funcionario funcionario);
}
