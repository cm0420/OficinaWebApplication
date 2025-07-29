package service;

import Repository.FuncionarioRepository;
import entity.Funcionario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {
    @Autowired
    private FuncionarioRepository funcionarioRepository;

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
        // Aqui você pode adicionar a lógica para criptografar a senha antes de salvar
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
}


