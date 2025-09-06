package miguel.projetos.oficina.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import miguel.projetos.oficina.Repository.CarroRepository;
import miguel.projetos.oficina.Repository.ClienteRepository;
import miguel.projetos.oficina.Repository.OrdemDeServicoRepository;
import miguel.projetos.oficina.entity.Carro;

@Service
public class CarroService {

    @Autowired private CarroRepository carroRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private IdGeneratorService idGeneratorService;
    @Autowired private OrdemDeServicoRepository ordemDeServicoRepository;

    @Transactional(readOnly = true)
    public List<Carro> findAll() {
        return carroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Carro> findById(String id) {
        return carroRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Carro> findByDonoCpf(String cpf) {
        String onlyDigits = cpf == null ? "" : cpf.replaceAll("\\D", "");
        return carroRepository.findCarroByDonoCpf(onlyDigits);
    }

    @Transactional
    public Carro save(Carro carro, String cpfDono) {
        String cpf = cpfDono.replaceAll("\\D", "");
        var dono = clienteRepository.findByCpf(cpf)
            .orElseThrow(() -> new IllegalArgumentException("Cliente com CPF " + cpf + " não encontrado."));

        carro.setPlaca(carro.getPlaca().trim().toUpperCase());
        carro.setChassi(carro.getChassi().trim().toUpperCase());

        if (carroRepository.existsByPlacaIgnoreCase(carro.getPlaca()))
            throw new IllegalArgumentException("Placa já cadastrada.");
        if (carroRepository.existsByChassi(carro.getChassi()))
            throw new IllegalArgumentException("Chassi já cadastrado.");

        Long next = idGeneratorService.getNextId("carro");
        carro.setId_carro("Carro-" + String.format("%03d", next));

        carro.setDono(dono);
        // Garantir estado "novo" (normalmente já está true em objetos recém-criados)
        carro.markNew();

        return carroRepository.save(carro); // agora será INSERT
    }

    @Transactional
    public Carro update(String id, Carro carroAtualizado) {
        Carro carroExistente = carroRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Carro com ID " + id + " não encontrado."));

        if (carroAtualizado.getFabricante() != null) {
            carroExistente.setFabricante(carroAtualizado.getFabricante());
        }
        if (carroAtualizado.getModelo() != null) {
            carroExistente.setModelo(carroAtualizado.getModelo());
        }
        if (carroAtualizado.getPlaca() != null) {
            String novaPlaca = carroAtualizado.getPlaca().trim().toUpperCase();
            if (!novaPlaca.equalsIgnoreCase(carroExistente.getPlaca())
                    && carroRepository.existsByPlacaIgnoreCase(novaPlaca)) {
                throw new IllegalArgumentException("Placa já cadastrada.");
            }
            carroExistente.setPlaca(novaPlaca);
        }

        // dono e chassi NÃO são alterados aqui
        return carroRepository.save(carroExistente);
    }

    @Transactional
    public void deleteById(String id) {
        Carro carro = carroRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Carro com ID " + id + " não encontrado."));

        if (!ordemDeServicoRepository.findByCarro(carro).isEmpty()) {
            throw new IllegalStateException("Não é possível remover um carro que possui ordens de serviço no histórico.");
        }

        carroRepository.deleteById(id);
    }
}
