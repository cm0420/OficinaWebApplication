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
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping
    public ResponseEntity<List<FuncionarioDto>> getAll() {
        List<FuncionarioDto> funcionarios = funcionarioService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<FuncionarioDto> getByCpf(@PathVariable String cpf) {
        // Lança uma exceção se não encontrar, que será tratada pelo RestExceptionHandler
        Funcionario funcionario = funcionarioService.findByCpf(cpf)
                .orElseThrow(() -> new NoSuchElementException("Funcionário com CPF " + cpf + " não encontrado."));
        return ResponseEntity.ok(convertToDto(funcionario));
    }

    @PostMapping
    public ResponseEntity<FuncionarioDto> create(@Valid @RequestBody FuncionarioCreateDto createDto) {
        // Se o CPF já existir, o serviço lança IllegalArgumentException (erro 400)
        Funcionario funcionario = convertCreateDtoToEntity(createDto);
        Funcionario novoFuncionario = funcionarioService.save(funcionario);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(novoFuncionario));
    }

    @PutMapping("/cpf/{cpf}")
    public ResponseEntity<FuncionarioDto> update(@PathVariable String cpf, @Valid @RequestBody FuncionarioDto updateDto) {
        // Se o funcionário não for encontrado, o serviço lança uma exceção (erro 404)
        Funcionario funcionario = convertToEntity(updateDto);
        Funcionario funcionarioAtualizado = funcionarioService.update(cpf, funcionario);
        return ResponseEntity.ok(convertToDto(funcionarioAtualizado));
    }

    @DeleteMapping("/cpf/{cpf}")
    public ResponseEntity<Void> delete(@PathVariable String cpf) {
        // O serviço irá lançar uma exceção se o CPF não existir, que será tratada pelo handler
        funcionarioService.delete(cpf);
        return ResponseEntity.noContent().build();
    }

    // --- MÉTODOS AUXILIARES ---

    private FuncionarioDto convertToDto(Funcionario funcionario) {
        FuncionarioDto dto = new FuncionarioDto();
        dto.setId_usuario(funcionario.getId_usuario());
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
        funcionario.setId_usuario(dto.getId_usuario());
        funcionario.setCpf(dto.getCpf());
        funcionario.setNome(dto.getNome());
        funcionario.setCargo(dto.getCargo());
        funcionario.setTelefone(dto.getTelefone());
        funcionario.setEndereco(dto.getEndereco());
        funcionario.setEmail(dto.getEmail());
        // A senha não é mapeada aqui, pois ela só deve ser definida na criação
        // ou num endpoint específico de atualização de senha.
        return funcionario;
    }

    private Funcionario convertCreateDtoToEntity(FuncionarioCreateDto createDto) {
        Funcionario funcionario = new Funcionario();
        funcionario.setCpf(createDto.getCpf());
        funcionario.setNome(createDto.getNome());
        funcionario.setSenha(createDto.getSenha()); // A senha é definida aqui
        funcionario.setCargo(createDto.getCargo());
        funcionario.setTelefone(createDto.getTelefone());
        funcionario.setEndereco(createDto.getEndereco());
        funcionario.setEmail(createDto.getEmail());
        return funcionario;
    }
}
