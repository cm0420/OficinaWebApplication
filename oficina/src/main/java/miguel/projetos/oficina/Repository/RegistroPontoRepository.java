package miguel.projetos.oficina.Repository;

import miguel.projetos.oficina.entity.Funcionario;
import miguel.projetos.oficina.entity.RegistroPonto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroPontoRepository extends CrudRepository<RegistroPonto, Long> {
    List<RegistroPonto> findByFuncionario(Funcionario funcionario);
    Optional<RegistroPonto> findFirstByFuncionarioAndDataHoraSaidaIsNullOrderByDataHoraEntradaDesc(Funcionario funcionario);
}
