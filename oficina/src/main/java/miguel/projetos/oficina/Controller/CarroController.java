package miguel.projetos.oficina.Controller;

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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carros")
public class CarroController {
    @Autowired
    private CarroService carroService;

    @GetMapping
    public ResponseEntity<List<CarroDto>> getAll() {
        List<CarroDto> carros = carroService.findAll().stream()
                .map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(carros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarroDto> getById(@PathVariable String id) {
        return carroService.findById(id)
                .map(carro -> ResponseEntity.ok(convertToDto(carro)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CarroDto> create(@Valid @RequestBody CarroCreateDto createDto) {
        try {
            Carro carro = convertCreateDtoToEntity(createDto);
            Carro novoCarro = carroService.save(carro, createDto.getCpfDono());
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(novoCarro));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarroDto> update(@PathVariable String id, @Valid @RequestBody CarroDto updateDto) {
        try {
            Carro carro = convertToEntity(updateDto);
            Carro carroAtualizado = carroService.update(id, carro);
            return ResponseEntity.ok(convertToDto(carroAtualizado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        carroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    private CarroDto convertToDto(Carro carro) {
        CarroDto dto = new CarroDto();
        dto.setId_carro(carro.getId_carro());
        dto.setChassi(carro.getChassi());
        dto.setPlaca(carro.getPlaca());
        dto.setFabricante(carro.getFabricante());
        dto.setModelo(carro.getModelo());

        // --- CORREÇÃO AQUI ---
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
        // O Dono precisaria ser tratado, provavelmente buscado e definido separadamente
        return carro;
    }

    private Carro convertCreateDtoToEntity(CarroCreateDto createDto) {
        Carro carro = new Carro();
        carro.setChassi(createDto.getChassi());
        carro.setPlaca(createDto.getPlaca());
        carro.setFabricante(createDto.getFabricante());
        carro.setModelo(createDto.getModelo());
        return carro;
    }
    private ClienteDto convertClienteToDto(Cliente cliente) {
        ClienteDto dto = new ClienteDto();
        dto.setCpf(cliente.getCpf());
        dto.setNome(cliente.getNome());
        dto.setTelefone(cliente.getTelefone());
        dto.setEndereco(cliente.getEndereco());
        dto.setEmail(cliente.getEmail());
        // O id_cliente não está no ClienteDto. Se precisar dele, adicione-o ao DTO.
        return dto;
    }
}
