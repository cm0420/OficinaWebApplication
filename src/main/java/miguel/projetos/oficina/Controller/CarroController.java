package miguel.projetos.oficina.Controller;

import miguel.projetos.oficina.Repository.ClienteRepository;
import miguel.projetos.oficina.dto.CarroCreateDto;
import miguel.projetos.oficina.dto.CarroDto;
import miguel.projetos.oficina.dto.ClienteDto;
import miguel.projetos.oficina.entity.Carro;
import miguel.projetos.oficina.entity.Cliente;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import miguel.projetos.oficina.service.CarroService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carros")
public class CarroController {
    @Autowired
    private CarroService carroService;

    // Repositório necessário para buscar a entidade Dono (Cliente)
    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public ResponseEntity<List<CarroDto>> getAll() {
        List<CarroDto> carros = carroService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(carros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarroDto> getById(@PathVariable String id) {
        // Lança uma exceção se não encontrar, que será tratada pelo RestExceptionHandler
        Carro carro = carroService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Carro com ID " + id + " não encontrado."));
        return ResponseEntity.ok(convertToDto(carro));
    }

    @PostMapping
    public ResponseEntity<CarroDto> create(@Valid @RequestBody CarroCreateDto createDto) {
        // A lógica de erro foi removida. Se o CPF do dono não existir,
        // o serviço lançará uma exceção que o handler global irá capturar.
        Carro carro = convertCreateDtoToEntity(createDto);
        Carro novoCarro = carroService.save(carro, createDto.getCpfDono());
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(novoCarro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarroDto> update(@PathVariable String id, @Valid @RequestBody CarroDto updateDto) {
        // O método convertToEntity agora está completo e busca o dono
        Carro carro = convertToEntity(updateDto);
        Carro carroAtualizado = carroService.update(id, carro);
        return ResponseEntity.ok(convertToDto(carroAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        // Se o carro não puder ser apagado (ex: por ter OS aberta),
        // o serviço deve lançar uma exceção que será tratada pelo handler.
        carroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // --- MÉTODOS AUXILIARES COMPLETOS ---

    private CarroDto convertToDto(Carro carro) {
        CarroDto dto = new CarroDto();
        dto.setId_carro(carro.getId_carro());
        dto.setChassi(carro.getChassi());
        dto.setPlaca(carro.getPlaca());
        dto.setFabricante(carro.getFabricante());
        dto.setModelo(carro.getModelo());

        if (carro.getDono() != null) {
            dto.setDono(convertClienteToDto(carro.getDono()));
        }

        return dto;
    }

    private Carro convertToEntity(CarroDto dto) {
        Carro carro = new Carro();
        carro.setId_carro(dto.getId_carro());
        carro.setChassi(dto.getChassi());
        carro.setPlaca(dto.getPlaca());
        carro.setFabricante(dto.getFabricante());
        carro.setModelo(dto.getModelo());

        // Lógica para associar o dono (Cliente) ao Carro
        if (dto.getDono() != null && dto.getDono().getCpf() != null) {
            Cliente dono = clienteRepository.findByCpf(dto.getDono().getCpf())
                    .orElseThrow(() -> new NoSuchElementException("Cliente (dono) com CPF " + dto.getDono().getCpf() + " não encontrado."));
            carro.setDono(dono);
        }

        return carro;
    }

    private Carro convertCreateDtoToEntity(CarroCreateDto createDto) {
        Carro carro = new Carro();
        carro.setChassi(createDto.getChassi());
        carro.setPlaca(createDto.getPlaca());
        carro.setFabricante(createDto.getFabricante());
        carro.setModelo(createDto.getModelo());
        // A entidade do dono é tratada e associada na camada de serviço (CarroService)
        return carro;
    }

    private ClienteDto convertClienteToDto(Cliente cliente) {
        ClienteDto dto = new ClienteDto();
        dto.setId_cliente(cliente.getId_cliente()); // Adicionado para consistência
        dto.setCpf(cliente.getCpf());
        dto.setNome(cliente.getNome());
        dto.setTelefone(cliente.getTelefone());
        dto.setEndereco(cliente.getEndereco());
        dto.setEmail(cliente.getEmail());
        return dto;
    }
}
