package miguel.projetos.oficina.service;

import miguel.projetos.oficina.Repository.FuncionarioRepository;
import miguel.projetos.oficina.entity.Funcionario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    @Autowired
    IdGeneratorService idGeneratorService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Funcionario> findAll() {
        return funcionarioRepository.findAll();
    }

    public Optional<Funcionario> findById(String id) {
        return funcionarioRepository.findById(id);
    }

    public Optional<Funcionario> findByCpf(String cpf) {

        return funcionarioRepository.findFuncionarioByCpf(cpf);
    }

    @Transactional
    public Funcionario save(Funcionario funcionario) {
        if (funcionarioRepository.findFuncionarioByCpf(funcionario.getCpf()).isPresent()) {
            throw new IllegalArgumentException("CPF de funcionário já cadastrado.");
        }
        String senhaCriptografada = passwordEncoder.encode(funcionario.getSenha());
        funcionario.setSenha(senhaCriptografada);

        Long proximoId = idGeneratorService.getNextId("funcionario");
        String idFormatado = "User-" + String.format("%03d", proximoId);
        funcionario.setId_usuario(idFormatado);

        return funcionarioRepository.save(funcionario);
    }

    @Transactional
    public void delete(String cpf) {
        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findFuncionarioByCpf(cpf);
        if (funcionarioOpt.isPresent()) {
            funcionarioRepository.delete(funcionarioOpt.get());

        } else {
            throw new IllegalStateException("Cpf do funcionario não existe");
        }
    }

    @Transactional
    public Funcionario update(String cpf, Funcionario funcionarioAtualizado) {
        Funcionario funcionarioExistente = funcionarioRepository.findFuncionarioByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário com CPF " + cpf + " não encontrado."));

        funcionarioExistente.setNome(funcionarioAtualizado.getNome());
        funcionarioExistente.setCargo(funcionarioAtualizado.getCargo());
        funcionarioExistente.setTelefone(funcionarioAtualizado.getTelefone());
        funcionarioExistente.setEndereco(funcionarioAtualizado.getEndereco());
        funcionarioExistente.setEmail(funcionarioAtualizado.getEmail());

        // A senha deve ser atualizada em um método separado por segurança

        return funcionarioRepository.save(funcionarioExistente);
    }
    @Transactional
    public Funcionario updatePassword(String id, String senhaAntiga, String senhaNova) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado."));

        // Verifica se a senha antiga fornecida corresponde à senha armazenada
        if (!passwordEncoder.matches(senhaAntiga, funcionario.getSenha())) {
            throw new IllegalArgumentException("A senha antiga está incorreta.");
        }

        // Criptografa e define a nova senha
        funcionario.setSenha(passwordEncoder.encode(senhaNova));

        return funcionarioRepository.save(funcionario);
    }
}


