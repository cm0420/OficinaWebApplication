package miguel.projetos.oficina.Controller;

import miguel.projetos.oficina.dto.FuncionarioCreateDto;
import miguel.projetos.oficina.dto.FuncionarioDto;
import miguel.projetos.oficina.entity.Funcionario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import miguel.projetos.oficina.service.FuncionarioService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    @Autowired
    FuncionarioService funcionarioService;

    @GetMapping
    public ResponseEntity<List<FuncionarioDto>> getAll() {
        List<FuncionarioDto> funcionarios = funcionarioService.findAll().stream()
                .map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(funcionarios);
    }
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<FuncionarioDto> getByCpf(@PathVariable String cpf) {
        return funcionarioService.findByCpf(cpf)
                .map(funcionario -> ResponseEntity.ok(convertToDto(funcionario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FuncionarioDto> create(@Valid @RequestBody FuncionarioCreateDto createDto) {
        try {
            Funcionario funcionario = convertCreateDtoToEntity(createDto);
            Funcionario novoFuncionario = funcionarioService.save(funcionario);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(novoFuncionario));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/cpf/{cpf}")
    public ResponseEntity<FuncionarioDto> update(@PathVariable String cpf, @Valid @RequestBody FuncionarioDto updateDto) {
        try {
            Funcionario funcionario = convertToEntity(updateDto);
            Funcionario funcionarioAtualizado = funcionarioService.update(cpf, funcionario);
            return ResponseEntity.ok(convertToDto(funcionarioAtualizado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/cpf/{cpf}")
    public ResponseEntity<Void> delete(@PathVariable String cpf) {
        try {
            funcionarioService.delete(cpf);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private FuncionarioDto convertToDto(Funcionario funcionario) {
        FuncionarioDto dto = new FuncionarioDto();
        dto.setId_usuario(funcionario.getId_usuario()); // Adicionar esta linha
        dto.setCpf(funcionario.getCpf());
        dto.setNome(funcionario.getNome());
        dto.setCargo(funcionario.getCargo());
        dto.setTelefone(funcionario.getTelefone());
        dto.setEndereco(funcionario.getEndereco());
        dto.setEmail(funcionario.getEmail());
        return dto;
    }

    private Funcionario convertToEntity(FuncionarioDto dto) {
        Funcionario funcionario = new Funcionario();
        funcionario.setId_usuario(dto.getId_usuario()); // Adicionar esta linha
        funcionario.setCpf(dto.getCpf());
        funcionario.setNome(dto.getNome());
        funcionario.setCargo(dto.getCargo());
        funcionario.setTelefone(dto.getTelefone());
        funcionario.setEndereco(dto.getEndereco());
        funcionario.setEmail(dto.getEmail());

        return funcionario;
    }

    private Funcionario convertCreateDtoToEntity(FuncionarioCreateDto createDto) {
        Funcionario funcionario = new Funcionario();
        funcionario.setCpf(createDto.getCpf());
        funcionario.setNome(createDto.getNome());
        funcionario.setSenha(createDto.getSenha());
        funcionario.setCargo(createDto.getCargo());
        funcionario.setTelefone(createDto.getTelefone());
        funcionario.setEndereco(createDto.getEndereco());
        funcionario.setEmail(createDto.getEmail());
        return funcionario;
    }
}
