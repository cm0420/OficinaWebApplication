package miguel.projetos.oficina.service;

import miguel.projetos.oficina.Repository.FuncionarioRepository;
import miguel.projetos.oficina.Repository.RegistroPontoRepository;
import miguel.projetos.oficina.entity.Funcionario;
import miguel.projetos.oficina.entity.RegistroPonto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PontoService {
    @Autowired
    private RegistroPontoRepository registroPontoRepository;

    // Injetar o repositório de funcionário para buscá-lo
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Transactional
    public RegistroPonto baterPontoEntrada(String cpf) {
        Funcionario funcionario = funcionarioRepository.findFuncionarioByCpf(cpf)
                .orElseThrow(() -> new NoSuchElementException("Funcionário com CPF " + cpf + " não encontrado."));

        Optional<RegistroPonto> pontoAberto = registroPontoRepository.findFirstByFuncionarioAndDataHoraSaidaIsNullOrderByDataHoraEntradaDesc(funcionario);

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

        RegistroPonto registroAberto = registroPontoRepository.findFirstByFuncionarioAndDataHoraSaidaIsNullOrderByDataHoraEntradaDesc(funcionario)
                .orElseThrow(() -> new IllegalStateException("Nenhum ponto de entrada em aberto encontrado para este funcionário."));

        registroAberto.setDataHoraSaida(LocalDateTime.now());
        return registroPontoRepository.save(registroAberto);
    }
}
