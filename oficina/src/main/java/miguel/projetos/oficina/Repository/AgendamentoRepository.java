package miguel.projetos.oficina.Repository;

import miguel.projetos.oficina.entity.Agendamento;
import miguel.projetos.oficina.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByMecanico(Funcionario mecanico);
    List<Agendamento> findByDataHoraBetween(LocalDateTime start, LocalDateTime end);
    boolean existsByMecanicoAndDataHora(Funcionario mecanico, LocalDateTime dataHora); // <-- CORRIGIDO

    Optional<Agendamento> findByMecanicoAndDataHora(Funcionario mecanico, LocalDateTime dataHora); // <-- CORRIGIDO

    Optional<Agendamento> findFirstByMecanicoAndDataHora(Funcionario mecanico, LocalDateTime dataHora);

}
