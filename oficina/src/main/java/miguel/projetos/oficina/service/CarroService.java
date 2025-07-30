package miguel.projetos.oficina.service;

import miguel.projetos.oficina.Repository.CarroRepository;
import miguel.projetos.oficina.Repository.ClienteRepository;
import miguel.projetos.oficina.entity.Carro;
import miguel.projetos.oficina.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.IllegalArgumentException;
import java.util.List;
import java.util.Optional;

@Service
public class CarroService {
    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private IdGeneratorService idGeneratorService;

    public List<Carro> findAll() {
        return carroRepository.findAll();
    }

    public Optional<Carro> findById(String id) {
        return carroRepository.findById(id);
    }

    public List<Carro> findByDonoCpf(String cpf) {
        return carroRepository.findCarroByDonoCpf(cpf);
    }

    @Transactional
    public Carro save(Carro carro, String cpfDono) {
        Cliente dono = clienteRepository.findByCpf(cpfDono)
                .orElseThrow(() -> new IllegalArgumentException("Cliente com CPF " + cpfDono + " não encontrado."));

        if (carroRepository.existsById(carro.getId_carro())) {
            throw new IllegalArgumentException("Carro com este ID já cadastrado.");
        }

        Long proximoId = idGeneratorService.getNextId("carro");
        String idFormatado = "Carro-" + String.format("%03d", proximoId);
        carro.setId_carro(idFormatado);

        carro.setDono(dono);
        return carroRepository.save(carro);
    }

    @Transactional
    public Carro update(String id, Carro carroAtualizado) {
        Carro carroExistente = carroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Carro com ID " + id + " não encontrado."));

        carroExistente.setFabricante(carroAtualizado.getFabricante());
        carroExistente.setModelo(carroAtualizado.getModelo());
        carroExistente.setPlaca(carroAtualizado.getPlaca());
        // O dono e o chassi não devem ser alterados aqui

        return carroRepository.save(carroExistente);
    }

    @Transactional
    public void deleteById(String id) {
        // Lógica de validação: Não permitir apagar carro com Ordens de Serviço
        // Esta validação deve ser adicionada aqui, consultando o OrdemDeServicoRepository
        carroRepository.deleteById(id);
    }
}
