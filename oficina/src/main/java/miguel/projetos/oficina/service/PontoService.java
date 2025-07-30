package miguel.projetos.oficina.service;

import miguel.projetos.oficina.Repository.RegistroPontoRepository;
import miguel.projetos.oficina.entity.Funcionario;
import miguel.projetos.oficina.entity.RegistroPonto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PontoService {
    @Autowired
    RegistroPontoRepository registroPontoRepository;

    @Transactional
    public RegistroPonto baterPontoEntrada(Funcionario funcionario) {
        // Regra de negócio: Verifica se já existe um ponto em aberto para o funcionário
        Optional<RegistroPonto> pontoAberto = registroPontoRepository.findFirstByFuncionarioAndData_hora_saidaIsNullOrderByData_hora_entradaDesc(funcionario);

        if (pontoAberto.isPresent()) {
            throw new IllegalStateException("Funcionário já possui um ponto em aberto.");
        }

        RegistroPonto novoRegistro = new RegistroPonto();
        novoRegistro.setFuncionario(funcionario);
        novoRegistro.setData_hora_entrada(LocalDateTime.now());
        return registroPontoRepository.save(novoRegistro);
    }
    @Transactional
    public RegistroPonto baterPontoSaida(Funcionario funcionario) {
        // Regra de negócio: Encontra o último ponto aberto para registrar a saída
        RegistroPonto registroAberto = registroPontoRepository.findFirstByFuncionarioAndData_hora_saidaIsNullOrderByData_hora_entradaDesc(funcionario)
                .orElseThrow(() -> new IllegalStateException("Nenhum ponto em aberto encontrado para este funcionário."));

        registroAberto.setData_hora_saida(LocalDateTime.now());
        return registroPontoRepository.save(registroAberto);
    }
}
