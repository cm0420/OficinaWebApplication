package miguel.projetos.oficina.Repository;

import miguel.projetos.oficina.entity.Funcionario;
import miguel.projetos.oficina.entity.RegistroPonto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroPontoRepository extends JpaRepository<RegistroPonto, Long> {

    // Buscar todos os registros de um funcionário
    List<RegistroPonto> findByFuncionario(Funcionario funcionario);

    // Buscar último ponto de entrada aberto
    Optional<RegistroPonto> findFirstByFuncionarioAndDataHoraSaidaIsNullOrderByDataHoraEntradaDesc(Funcionario funcionario);

    // Buscar pontos dentro de um intervalo de datas
    List<RegistroPonto> findByFuncionarioAndDataHoraEntradaBetween(
            Funcionario funcionario,
            LocalDateTime inicio,
            LocalDateTime fim
    );
}
