package service;

import Repository.CarroRepository;
import Repository.ClienteRepository;
import entity.Cliente;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private CarroRepository carroRepository;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(String id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> findByCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    @Transactional
    public Cliente save(Cliente cliente) {
        if (clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void delete(String cpf) {
        Optional<Cliente> clienteOpt = clienteRepository.findByCpf(cpf);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            if (carroRepository.findCarroByDonoCpf(cliente.getCpf()).isEmpty()) {
                clienteRepository.deleteClienteByCpf(cpf);
            } else {
                throw new IllegalStateException("Não é possível remover um cliente que possui veículos cadastrados.");
            }
        }
    }

    @Transactional
    public Cliente update(String cpf, Cliente clienteAtualizado) {

        Cliente clienteExistente = clienteRepository.deleteClienteByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Cliente com cpf " + cpf + " não encontrado."));


        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setTelefone(clienteAtualizado.getTelefone());
        clienteExistente.setEndereco(clienteAtualizado.getEndereco());
        clienteExistente.setEmail(clienteAtualizado.getEmail());

        return clienteRepository.save(clienteExistente);
    }
}