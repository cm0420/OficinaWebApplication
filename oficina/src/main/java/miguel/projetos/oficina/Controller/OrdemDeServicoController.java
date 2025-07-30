package miguel.projetos.oficina.Controller;

import miguel.projetos.oficina.dto.OrdemDeServicoCreateDto;
import miguel.projetos.oficina.dto.OrdemDeServicoDto;
import miguel.projetos.oficina.entity.OrdemDeServico;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import miguel.projetos.oficina.service.OrdemDeServicoService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/os")
public class OrdemDeServicoController {
    @Autowired
    private OrdemDeServicoService ordemDeServicoService;

    @GetMapping
    public ResponseEntity<List<OrdemDeServicoDto>> getAll() {
        List<OrdemDeServicoDto> osList = ordemDeServicoService.findAll().stream()
                .map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(osList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemDeServicoDto> getById(@PathVariable String id) {
        return ordemDeServicoService.findById(id)
                .map(os -> ResponseEntity.ok(convertToDto(os)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrdemDeServicoDto> create(@Valid @RequestBody OrdemDeServicoCreateDto createDto) {
        try {
            OrdemDeServico novaOS = ordemDeServicoService.abrirOS(createDto.getAgendamentoId(), createDto.getDefeitoRelatado());
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(novaOS));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/adicionar-peca")
    public ResponseEntity<OrdemDeServicoDto> adicionarPeca(@PathVariable String id, @RequestParam String pecaId, @RequestParam int quantidade) {
        try {
            OrdemDeServico os = ordemDeServicoService.adicionarPeca(id, pecaId, quantidade);
            return ResponseEntity.ok(convertToDto(os));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}/iniciar-inspecao")
    public ResponseEntity<OrdemDeServicoDto> iniciarInspecao(@PathVariable String id) {
        try {
            OrdemDeServico os = ordemDeServicoService.iniciarInspecao(id);
            return ResponseEntity.ok(convertToDto(os));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/iniciar-servico")
    public ResponseEntity<OrdemDeServicoDto> iniciarServico(@PathVariable String id) {
        try {
            OrdemDeServico os = ordemDeServicoService.iniciarServico(id);
            return ResponseEntity.ok(convertToDto(os));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/finalizar-servico")
    public ResponseEntity<OrdemDeServicoDto> finalizarServico(@PathVariable String id) {
        try {
            OrdemDeServico os = ordemDeServicoService.finalizarServico(id);
            return ResponseEntity.ok(convertToDto(os));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<OrdemDeServicoDto> cancelarOS(@PathVariable String id) {
        try {
            OrdemDeServico os = ordemDeServicoService.cancelarOS(id);
            return ResponseEntity.ok(convertToDto(os));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private OrdemDeServicoDto convertToDto(OrdemDeServico os) {
        OrdemDeServicoDto dto = new OrdemDeServicoDto();
        dto.setNumero_os(os.getNumero_os());
        dto.setDefeito_relatado(os.getDefeito_relatado());
        dto.setData_abertura(os.getData_abertura());
        dto.setData_fechamento(os.getData_fechamento());
        dto.setStatus(os.getStatus());
        // Conversões de DTOs aninhados são necessárias
        return dto;
    }
}
