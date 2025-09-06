    package miguel.projetos.oficina.Controller;
    
    import miguel.projetos.oficina.dto.ClienteCreateDto;
    import miguel.projetos.oficina.dto.ClienteDto;
    import miguel.projetos.oficina.entity.Cliente;
    import jakarta.validation.Valid;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import miguel.projetos.oficina.service.ClienteService;
    
    import java.util.List;
    import java.util.NoSuchElementException; // Example for an exception thrown by service
    
    @RestController
    @RequestMapping("/api/clientes")
    public class ClienteController {

        @Autowired
        private ClienteService clienteService;

        @GetMapping
        public ResponseEntity<List<ClienteDto>> getAll() {
            List<ClienteDto> clientes = clienteService.findAll().stream()
                    .map(this::convertToDto)
                    .toList(); // Para Java < 16, usar .collect(Collectors.toList())
            return ResponseEntity.ok(clientes);
        }

        @GetMapping("/cpf/{cpf}")
        public ResponseEntity<ClienteDto> getClienteByCpf(@PathVariable String cpf) {
            // Agora lança uma exceção se não encontrar, que será convertida para um erro 404
            Cliente cliente = clienteService.findByCpf(cpf)
                    .orElseThrow(() -> new NoSuchElementException("Cliente com CPF " + cpf + " não encontrado."));
            return ResponseEntity.ok(convertToDto(cliente));
        }

        @PostMapping
        public ResponseEntity<ClienteDto> create(@Valid @RequestBody ClienteCreateDto clienteCreateDto) {
            // Se o CPF já existir, o serviço lança IllegalArgumentException (erro 400)
            Cliente clienteParaSalvar = convertCreateDtoToEntity(clienteCreateDto);
            Cliente clienteSalvo = clienteService.save(clienteParaSalvar);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(clienteSalvo));
        }

        @PutMapping("/cpf/{cpf}")
        public ResponseEntity<ClienteDto> updateCliente(@PathVariable String cpf, @Valid @RequestBody ClienteDto updateDto) {
            // Sem try-catch. Se não encontrar, o serviço lança uma exceção (erro 404)
            Cliente clienteAtualizado = clienteService.update(cpf, updateDto);
            return ResponseEntity.ok(convertToDto(clienteAtualizado));
        }

        @DeleteMapping("/cpf/{cpf}")
        public ResponseEntity<Void> deleteCliente(@PathVariable String cpf) {
            // Sem try-catch. Se não encontrar (erro 404) ou se houver conflito (erro 409),
            // as exceções serão tratadas pelo handler global.
            clienteService.delete(cpf);
            return ResponseEntity.noContent().build();
        }

        // --- MÉTODOS AUXILIARES ---

        private ClienteDto convertToDto(Cliente cliente) {
            ClienteDto dto = new ClienteDto();
            dto.setId_cliente(cliente.getId_cliente()); // Importante incluir o ID no DTO
            dto.setCpf(cliente.getCpf());
            dto.setNome(cliente.getNome());
            dto.setTelefone(cliente.getTelefone());
            dto.setEndereco(cliente.getEndereco());
            dto.setEmail(cliente.getEmail());
            return dto;
        }

        private Cliente convertCreateDtoToEntity(ClienteCreateDto createDto) {
            Cliente cliente = new Cliente();
            cliente.setCpf(createDto.getCpf());
            cliente.setNome(createDto.getNome());
            cliente.setTelefone(createDto.getTelefone());
            cliente.setEndereco(createDto.getEndereco());
            cliente.setEmail(createDto.getEmail());
            return cliente;
        }
    }