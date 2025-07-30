package miguel.projetos.oficina.Controller;

import miguel.projetos.oficina.dto.AgendamentoCreateDto;
import miguel.projetos.oficina.dto.AgendamentoDto;
import miguel.projetos.oficina.entity.Agendamento;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import miguel.projetos.oficina.service.AgendamentoService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agendamento")
public class AgendamentoController {
    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping
    public ResponseEntity<List<AgendamentoDto>> getAll() {
        List<AgendamentoDto> agendamentos = agendamentoService.findAll().stream()
                .map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoDto> getById(@PathVariable Long id) {
        return agendamentoService.findById(id)
                .map(agendamento -> ResponseEntity.ok(convertToDto(agendamento)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AgendamentoDto> create(@Valid @RequestBody AgendamentoCreateDto createDto) {
        try {
            Agendamento agendamento = convertCreateDtoToEntity(createDto);
            Agendamento novoAgendamento = agendamentoService.save(agendamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(novoAgendamento));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoDto> update(@PathVariable Long id, @Valid @RequestBody AgendamentoDto updateDto) {
        try {
            Agendamento agendamento = convertToEntity(updateDto);
            Agendamento agendamentoAtualizado = agendamentoService.update(id, agendamento);
            return ResponseEntity.ok(convertToDto(agendamentoAtualizado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            agendamentoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private AgendamentoDto convertToDto(Agendamento agendamento) {
        AgendamentoDto dto = new AgendamentoDto();
        dto.setId(agendamento.getId());
        dto.setData_hora(agendamento.getData_hora());
        dto.setTipo_servico(agendamento.getTipo_sevico());
        // A conversão de DTOs aninhados (cliente, carro, mecanico) precisaria ser implementada.
        return dto;
    }

    private Agendamento convertToEntity(AgendamentoDto dto) {
        Agendamento agendamento = new Agendamento();
        agendamento.setId(dto.getId());
        agendamento.setData_hora(dto.getData_hora());
        agendamento.setTipo_sevico(dto.getTipo_servico());
        // A conversão de entidades aninhadas precisaria ser implementada.
        return agendamento;
    }

    private Agendamento convertCreateDtoToEntity(AgendamentoCreateDto createDto) {
        Agendamento agendamento = new Agendamento();
        agendamento.setData_hora(createDto.getData());
        agendamento.setTipo_sevico(createDto.getTipo_servico());
        // Você precisaria buscar as entidades relacionadas (Cliente, Carro, Funcionario)
        // usando os IDs do DTO e defini-los na entidade Agendamento.
        return agendamento;
    }
}
