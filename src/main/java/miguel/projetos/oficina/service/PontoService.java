package miguel.projetos.oficina.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import miguel.projetos.oficina.Repository.FuncionarioRepository;
import miguel.projetos.oficina.Repository.RegistroPontoRepository;
import miguel.projetos.oficina.dto.PontoDto;
import miguel.projetos.oficina.dto.ResumoPontoDto;
import miguel.projetos.oficina.entity.Funcionario;
import miguel.projetos.oficina.entity.RegistroPonto;

@Service
public class PontoService {

    @Autowired
    private RegistroPontoRepository registroPontoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    // ========= MÉTODOS EXISTENTES =========

    @Transactional
    public RegistroPonto baterPontoEntrada(String cpf) {
        Funcionario funcionario = funcionarioRepository.findFuncionarioByCpf(cpf)
                .orElseThrow(() -> new NoSuchElementException("Funcionário com CPF " + cpf + " não encontrado."));

        Optional<RegistroPonto> pontoAberto =
                registroPontoRepository.findFirstByFuncionarioAndDataHoraSaidaIsNullOrderByDataHoraEntradaDesc(funcionario);

        if (pontoAberto.isPresent()) {
            throw new IllegalStateException("Funcionário já possui um ponto de entrada em aberto.");
        }

        RegistroPonto novoRegistro = new RegistroPonto();
        novoRegistro.setFuncionario(funcionario);
        novoRegistro.setDataHoraEntrada(LocalDateTime.now());
        return registroPontoRepository.save(novoRegistro);
    }

    @Transactional
    public RegistroPonto baterPontoSaida(String cpf) {
        Funcionario funcionario = funcionarioRepository.findFuncionarioByCpf(cpf)
                .orElseThrow(() -> new NoSuchElementException("Funcionário com CPF " + cpf + " não encontrado."));

        RegistroPonto registroAberto = registroPontoRepository
                .findFirstByFuncionarioAndDataHoraSaidaIsNullOrderByDataHoraEntradaDesc(funcionario)
                .orElseThrow(() -> new IllegalStateException("Nenhum ponto de entrada em aberto encontrado para este funcionário."));

        registroAberto.setDataHoraSaida(LocalDateTime.now());
        return registroPontoRepository.save(registroAberto);
    }

    // ========= NOVOS MÉTODOS =========

    /**
     * Lista todos os pontos batidos de um funcionário em determinado mês.
     */
    public List<PontoDto> getPontosDoFuncionario(String cpf, int ano, int mes) {
        Funcionario funcionario = funcionarioRepository.findFuncionarioByCpf(cpf)
                .orElseThrow(() -> new NoSuchElementException("Funcionário não encontrado."));

        LocalDate inicio = LocalDate.of(ano, mes, 1);
        LocalDate fim = inicio.plusMonths(1);

        List<RegistroPonto> pontos = registroPontoRepository
                .findByFuncionarioAndDataHoraEntradaBetween(funcionario, inicio.atStartOfDay(), fim.atStartOfDay());

        return pontos.stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Calcula o resumo mensal de dias, horas e extras de um funcionário.
     */
    public ResumoPontoDto getResumoMensal(String cpf, int ano, int mes) {
        List<PontoDto> pontos = getPontosDoFuncionario(cpf, ano, mes);

        long horasTrabalhadas = pontos.stream().mapToLong(PontoDto::getHorasTrabalhadas).sum();
        long diasTrabalhados = pontos.stream()
                .filter(p -> p.getHorasTrabalhadas() > 0)
                .count();

        // Exemplo: 160h como carga mensal (40h/semana). Ajuste conforme sua regra.
        long cargaHorariaMensal = 160;
        long horasExtras = horasTrabalhadas > cargaHorariaMensal ? horasTrabalhadas - cargaHorariaMensal : 0;

        ResumoPontoDto resumo = new ResumoPontoDto();
        resumo.setCpf(cpf);
        resumo.setAno(ano);
        resumo.setMes(mes);
        resumo.setDiasTrabalhados(diasTrabalhados);
        resumo.setHorasTrabalhadas(horasTrabalhadas);
        resumo.setHorasExtras(horasExtras);

        return resumo;
    }

    /**
     * Relatório consolidado de todos os funcionários no mês.
     */
    public List<ResumoPontoDto> getRelatorioMensal(int ano, int mes) {
        return funcionarioRepository.findAll().stream()
                .map(f -> getResumoMensal(f.getCpf(), ano, mes))
                .collect(Collectors.toList());
    }

    // ========= HELPER =========

    private PontoDto toDto(RegistroPonto registro) {
        PontoDto dto = new PontoDto();
        dto.setEntrada(registro.getDataHoraEntrada());
        dto.setSaida(registro.getDataHoraSaida());

        if (registro.getDataHoraEntrada() != null && registro.getDataHoraSaida() != null) {
            long horas = Duration.between(registro.getDataHoraEntrada(), registro.getDataHoraSaida()).toHours();
            dto.setHorasTrabalhadas(horas);
        } else {
            dto.setHorasTrabalhadas(0);
        }

        return dto;
    }
}
