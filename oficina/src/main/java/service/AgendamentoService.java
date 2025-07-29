package service;

import Repository.AgendamentoRepository;
import entity.Agendamento;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {
    @Autowired
    private AgendamentoRepository agendamentoRepository;
    @Autowired
    private FinanceiroService financeiroService;

    public List<Agendamento> findAll() {
        return agendamentoRepository.findAll();
    }
    public Optional<Agendamento> findById(Long id) {
        return agendamentoRepository.findById(id);
    }
    public List<Agendamento> findByData(LocalDate data) {
        LocalDateTime inicioDoDia = data.atStartOfDay();
        LocalDateTime fimDoDia = data.atTime(23, 59, 59);
        return agendamentoRepository.findByDataHoraBetween(inicioDoDia, fimDoDia);
    }
    @Transactional
    public Agendamento save(Agendamento agendamento) {
        // 1. Valida o horário de funcionamento
        validarHorarioFuncionamento(agendamento.getData_hora());

        // 2. Valida se o mecânico já tem um agendamento nesse horário
        if (agendamentoRepository.existsByMecanicoAndData_hora(agendamento.getMecanico(), agendamento.getData_hora())) {
            throw new IllegalStateException("O mecânico selecionado já possui um agendamento neste horário.");
        }

        return agendamentoRepository.save(agendamento);
    }


    @Transactional
    public Agendamento update(Long id, Agendamento agendamentoAtualizado) {
        Agendamento agendamentoExistente = agendamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento com ID " + id + " não encontrado."));

        // Valida o novo horário
        validarHorarioFuncionamento(agendamentoAtualizado.getData_hora());

        Optional<Agendamento> agendamentoConflitanteOpt = agendamentoRepository.findByMecanicoAndData_hora(
                agendamentoAtualizado.getMecanico(), agendamentoAtualizado.getData_hora()
        );
        // Valida conflito de horário, mas permite a alteração se o conflito for com o próprio agendamento que está a ser editado.
        if (agendamentoConflitanteOpt.isPresent() && !agendamentoConflitanteOpt.get().getId().equals(id)) {
            throw new IllegalStateException("O mecânico selecionado já possui um outro agendamento neste horário.");
        }

        // Atualiza os campos
        agendamentoExistente.setCliente(agendamentoAtualizado.getCliente());
        agendamentoExistente.setCarro(agendamentoAtualizado.getCarro());
        agendamentoExistente.setMecanico(agendamentoAtualizado.getMecanico());
        agendamentoExistente.setData_hora(agendamentoAtualizado.getData_hora());
        agendamentoExistente.setTipo_sevico(agendamentoAtualizado.getTipo_sevico());

        return agendamentoRepository.save(agendamentoExistente);
    }

    public void deleteById(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado."));

        // Regra de negócio: Se o cancelamento for no mesmo dia, registrar taxa.
        if (agendamento.getData_hora().toLocalDate().isEqual(LocalDate.now())) {
            String motivo = "Cancelamento no dia do serviço (" + LocalDate.now() + ")";
            financeiroService.registrarTaxaCancelamento(agendamento.getCliente(), motivo);
        }

        agendamentoRepository.deleteById(id);
    }

    private void validarHorarioFuncionamento(LocalDateTime dataHora) {
        LocalTime horario = dataHora.toLocalTime();

        // Define os limites do expediente
        LocalTime inicioManha = LocalTime.of(8, 0);
        LocalTime fimManha = LocalTime.of(12, 0);
        LocalTime inicioTarde = LocalTime.of(14, 0);
        LocalTime fimTarde = LocalTime.of(18, 0);

        // Verifica se o horário está no período da manhã (inclusive 8:00, exclusivo 12:00)
        boolean horarioManhaValido = !horario.isBefore(inicioManha) && horario.isBefore(fimManha);

        // Verifica se o horário está no período da tarde (inclusive 14:00, exclusivo 18:00)
        boolean horarioTardeValido = !horario.isBefore(inicioTarde) && horario.isBefore(fimTarde);

        if (!horarioManhaValido && !horarioTardeValido) {
            throw new IllegalArgumentException("O horário do agendamento deve ser entre 08:00-12:00 ou 14:00-18:00.");
        }
    }
}
