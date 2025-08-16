package miguel.projetos.oficina.Controller;

import miguel.projetos.oficina.Repository.CarroRepository;
import miguel.projetos.oficina.Repository.ClienteRepository;
import miguel.projetos.oficina.Repository.FuncionarioRepository;
import miguel.projetos.oficina.dto.AgendamentoCreateDto;
import miguel.projetos.oficina.dto.AgendamentoDto;
import miguel.projetos.oficina.dto.CarroDto;
import miguel.projetos.oficina.dto.ClienteDto;
import miguel.projetos.oficina.dto.FuncionarioDto;
import miguel.projetos.oficina.entity.Agendamento;
import miguel.projetos.oficina.entity.Carro;
import miguel.projetos.oficina.entity.Cliente;
import miguel.projetos.oficina.entity.Funcionario;
import miguel.projetos.oficina.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agendamento")
public class AgendamentoController {
    @Autowired
    private AgendamentoService agendamentoService;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private CarroRepository carroRepository;
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @GetMapping
    public ResponseEntity<List<AgendamentoDto>> getAll() {
        List<AgendamentoDto> agendamentos = agendamentoService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoDto> getById(@PathVariable Long id) {
        // Lança NoSuchElementException se não encontrar, que será tratado pelo RestExceptionHandler
        Agendamento agendamento = agendamentoService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Agendamento com ID " + id + " não encontrado."));
        return ResponseEntity.ok(convertToDto(agendamento));
    }
    @PostMapping("/mecanico/{cpf}/puxar-proximo")
    public ResponseEntity<AgendamentoDto> puxarProximoAgendamento(@PathVariable String cpf) {
        Agendamento agendamentoAdiantado = agendamentoService.puxarProximoAgendamento(cpf);
        return ResponseEntity.ok(convertToDto(agendamentoAdiantado));
    } 

    @PostMapping
    public ResponseEntity<AgendamentoDto> create(@Valid @RequestBody AgendamentoCreateDto createDto) {
        // Lógica de conversão agora busca as entidades no banco de dados
        Agendamento agendamento = convertCreateDtoToEntity(createDto);
        Agendamento novoAgendamento = agendamentoService.save(agendamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(novoAgendamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoDto> update(@PathVariable Long id, @Valid @RequestBody AgendamentoDto updateDto) {
        // Lógica de conversão busca as entidades para garantir a consistência
        Agendamento agendamento = convertToEntity(updateDto);
        Agendamento agendamentoAtualizado = agendamentoService.update(id, agendamento);
        return ResponseEntity.ok(convertToDto(agendamentoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // O serviço lançará a exceção se o ID não existir, e o handler global tratará
        agendamentoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }



    private AgendamentoDto convertToDto(Agendamento agendamento) {
        AgendamentoDto dto = new AgendamentoDto();
        dto.setId(agendamento.getId());
        dto.setData_hora(agendamento.getDataHora());
        dto.setTipo_servico(agendamento.getTipoServico());

        // Conversão completa dos objetos aninhados
        if (agendamento.getCliente() != null) {
            dto.setCliente(convertClienteToDto(agendamento.getCliente()));
        }
        if (agendamento.getCarro() != null) {
            dto.setCarro(convertCarroToDto(agendamento.getCarro()));
        }
        if (agendamento.getMecanico() != null) {
            dto.setMecanico(convertFuncionarioToDto(agendamento.getMecanico()));
        }

        return dto;
    }

    private Agendamento convertToEntity(AgendamentoDto dto) {
        Agendamento agendamento = new Agendamento();
        agendamento.setId(dto.getId());
        agendamento.setDataHora(dto.getData_hora());
        agendamento.setTipoServico(dto.getTipo_servico());

        // Busca as entidades relacionadas para garantir que elas existam
        if (dto.getCliente() != null && dto.getCliente().getCpf() != null) {
            Cliente cliente = clienteRepository.findByCpf(dto.getCliente().getCpf())
                    .orElseThrow(() -> new NoSuchElementException("Cliente com CPF " + dto.getCliente().getCpf() + " não encontrado."));
            agendamento.setCliente(cliente);
        }
        if (dto.getCarro() != null && dto.getCarro().getId_carro() != null) {
            Carro carro = carroRepository.findById(dto.getCarro().getId_carro())
                    .orElseThrow(() -> new NoSuchElementException("Carro com ID " + dto.getCarro().getId_carro() + " não encontrado."));
            agendamento.setCarro(carro);
        }
        if (dto.getMecanico() != null && dto.getMecanico().getId_usuario() != null) {
            Funcionario mecanico = funcionarioRepository.findById(dto.getMecanico().getId_usuario())
                    .orElseThrow(() -> new NoSuchElementException("Mecânico com ID " + dto.getMecanico().getId_usuario() + " não encontrado."));
            agendamento.setMecanico(mecanico);
        }

        return agendamento;
    }

    private Agendamento convertCreateDtoToEntity(AgendamentoCreateDto createDto) {
        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(createDto.getData());
        agendamento.setTipoServico(createDto.getTipo_servico());

        // Busca as entidades relacionadas usando os IDs fornecidos no DTO de criação
        Cliente cliente = clienteRepository.findById(createDto.getId_cliente())
                .orElseThrow(() -> new NoSuchElementException("Cliente com ID " + createDto.getId_cliente() + " não encontrado para o agendamento."));
        Carro carro = carroRepository.findById(createDto.getId_carro())
                .orElseThrow(() -> new NoSuchElementException("Carro com ID " + createDto.getId_carro() + " não encontrado para o agendamento."));
        Funcionario mecanico = funcionarioRepository.findById(createDto.getId_mecanico())
                .orElseThrow(() -> new NoSuchElementException("Mecânico com ID " + createDto.getId_mecanico() + " não encontrado para o agendamento."));

        agendamento.setCliente(cliente);
        agendamento.setCarro(carro);
        agendamento.setMecanico(mecanico);

        return agendamento;
    }

    // --- Conversores para DTOs aninhados ---

    private ClienteDto convertClienteToDto(Cliente cliente) {
        ClienteDto dto = new ClienteDto();
        dto.setId_cliente(cliente.getId_cliente());
        dto.setCpf(cliente.getCpf());
        dto.setNome(cliente.getNome());
        dto.setTelefone(cliente.getTelefone());
        dto.setEmail(cliente.getEmail());
        dto.setEndereco(cliente.getEndereco());
        return dto;
    }

    private CarroDto convertCarroToDto(Carro carro) {
        CarroDto dto = new CarroDto();
        dto.setId_carro(carro.getId_carro());
        dto.setChassi(carro.getChassi());
        dto.setPlaca(carro.getPlaca());
        dto.setFabricante(carro.getFabricante());
        dto.setModelo(carro.getModelo());
        // Evita chamadas recursivas, o dono já está no nível superior do AgendamentoDto
        return dto;
    }

    private FuncionarioDto convertFuncionarioToDto(Funcionario funcionario) {
        FuncionarioDto dto = new FuncionarioDto();
        dto.setId_usuario(funcionario.getId_usuario());
        dto.setCpf(funcionario.getCpf());
        dto.setNome(funcionario.getNome());
        dto.setCargo(funcionario.getCargo());
        return dto;
    }
}
