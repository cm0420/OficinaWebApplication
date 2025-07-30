package miguel.projetos.oficina.Controller;

import miguel.projetos.oficina.dto.PecaDto;
import miguel.projetos.oficina.entity.Peca;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import miguel.projetos.oficina.service.EstoqueService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pecas")
public class PecaController {
    @Autowired
    private EstoqueService estoqueService;

    @GetMapping
    public ResponseEntity<List<PecaDto>> getAll() {
        List<PecaDto> pecas = estoqueService.findAll().stream()
                .map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(pecas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PecaDto> getById(@PathVariable String id) {
        return estoqueService.findById(id)
                .map(peca -> ResponseEntity.ok(convertToDto(peca)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PecaDto> create(@Valid @RequestBody PecaDto pecaDto) {
        Peca peca = convertToEntity(pecaDto);
        Peca novaPeca = estoqueService.cadastrarNovaPeca(peca);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(novaPeca));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PecaDto> update(@PathVariable String id, @Valid @RequestBody PecaDto pecaDto) {
        try {
            Peca peca = convertToEntity(pecaDto);
            Peca pecaAtualizada = estoqueService.update(id, peca);
            return ResponseEntity.ok(convertToDto(pecaAtualizada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/repor-estoque")
    public ResponseEntity<PecaDto> reporEstoque(@PathVariable String id, @RequestParam int quantidade) {
        try {
            Peca peca = estoqueService.reporEstoque(id, quantidade);
            return ResponseEntity.ok(convertToDto(peca));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private PecaDto convertToDto(Peca peca) {
        PecaDto dto = new PecaDto();
        dto.setId_produto(peca.getId_produto());
        dto.setNome(peca.getNome());
        dto.setPreco(peca.getPreco());
        dto.setQuantidade(peca.getQuantidade());
        dto.setFornecedor(peca.getFornecedor());
        return dto;
    }

    private Peca convertToEntity(PecaDto dto) {
        Peca peca = new Peca();
        peca.setId_produto(dto.getId_produto());
        peca.setNome(dto.getNome());
        peca.setPreco(dto.getPreco());
        peca.setQuantidade(dto.getQuantidade());
        peca.setFornecedor(dto.getFornecedor());
        return peca;
    }
}
