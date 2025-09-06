package miguel.projetos.oficina.service;

import jakarta.transaction.Transactional;
import miguel.projetos.oficina.Repository.CarroRepository;
import miguel.projetos.oficina.Repository.ClienteRepository;
import miguel.projetos.oficina.Repository.OrdemDeServicoRepository;
import miguel.projetos.oficina.dto.ClienteDto;
import miguel.projetos.oficina.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private CarroRepository carroRepository;
    @Autowired
    IdGeneratorService idGeneratorService;
    @Autowired
    OrdemDeServicoRepository ordemDeServicoRepository;

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
        // Encontra o cliente ou lança uma exceção se não existir
        Cliente cliente = clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new NoSuchElementException("Cliente com CPF " + cpf + " não encontrado."));
        if (!ordemDeServicoRepository.findByCliente(cliente).isEmpty()) {
            throw new IllegalStateException("Não é possível remover um cliente que possui ordens de serviço no histórico.");
        }
        if (!carroRepository.findCarroByDonoCpf(cliente.getCpf()).isEmpty()) {
            throw new IllegalStateException("Não é possível remover um cliente que possui veículos cadastrados.");
        }
        // Se passar em todas as validações, deleta o cliente.
        clienteRepository.delete(cliente);
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