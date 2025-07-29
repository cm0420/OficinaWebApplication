package Controller;

import dto.ClienteCreateDto;
import dto.ClienteDto;
import entity.Cliente;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ClienteService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    @Autowired
    ClienteService clienteService;


    @GetMapping
    public ResponseEntity<List<ClienteDto>> getAll() {
        List<ClienteDto> clientes = clienteService.findAll().stream()
                .map(this::convertToDto).toList();
        return ResponseEntity.ok(clientes);
    }
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteDto> getClienteByCpf(@PathVariable String cpf) {
        return clienteService.findByCpf(cpf).map(cliente -> ResponseEntity.ok(convertToDto(cliente)))
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<ClienteDto> create(@Valid @RequestBody ClienteCreateDto clienteCreateDto) {
        Cliente clienteParaSalvar = convertCreateDtoToEntity(createDto);
        Cliente clienteSalvo = clienteService.save(clienteParaSalvar);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(clienteSalvo));
    }
    @PutMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteDto> updateCliente(@PathVariable String cpf, @Valid @RequestBody ClienteDto updateDto){
        Cliente clienteAtualizado = clienteService.update(cpf, updateDto );
        return ResponseEntity.ok(convertToDto(clienteAtualizado));
    }
    @DeleteMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteDto> deleteCliente(@PathVariable String cpf){
        try {
            clienteService.delete(cpf);
            return ResponseEntity.noContent().build();
        }catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }




    private ClienteDto convertToDto(Cliente cliente) {
        ClienteDto dto = new ClienteDto();

        dto.setCpf(cliente.getCpf());
        dto.setNome(cliente.getNome());
        dto.setTelefone(cliente.getTelefone());
        dto.setEndereco(cliente.getEndereco());
        dto.setEmail(cliente.getEmail());
        return dto;
    }
    private Cliente convertToEntity(ClienteCreateDto createDto) {
        Cliente cliente = new Cliente();
        // O ID não é definido aqui, pois será gerado pelo serviço
        cliente.setCpf(createDto.getCpf());
        cliente.setNome(createDto.getNome());
        cliente.setTelefone(createDto.getTelefone());
        cliente.setEndereco(createDto.getEndereco());
        cliente.setEmail(createDto.getEmail());
        return cliente;
    }

}
