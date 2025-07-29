package service;

import Repository.CarroRepository;
import Repository.ClienteRepository;
import dto.ClienteDto;
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
    @Autowired
    IdGeneratorService idGeneratorService;

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
        Long proximoId = idGeneratorService.getNextId("cliente");

        String idFormatado = "CL-" + String.format("%03d", proximoId);
        cliente.setId_cliente(idFormatado);

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
    public Cliente update(String cpf, ClienteDto clienteAtualizadoDto) {

        Cliente clienteExistente = clienteRepository.deleteClienteByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Cliente com cpf " + cpf + " não encontrado."));


        clienteExistente.setNome(clienteAtualizadoDto.getNome());
        clienteExistente.setTelefone(clienteAtualizadoDto.getTelefone());
        clienteExistente.setEndereco(clienteAtualizadoDto.getEndereco());
        clienteExistente.setEmail(clienteAtualizadoDto.getEmail());

        return clienteRepository.save(clienteExistente);
    }
}