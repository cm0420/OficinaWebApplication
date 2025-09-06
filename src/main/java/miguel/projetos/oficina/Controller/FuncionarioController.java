package miguel.projetos.oficina.Controller;

import miguel.projetos.oficina.dto.FuncionarioCreateDto;
import miguel.projetos.oficina.dto.FuncionarioDto;
import miguel.projetos.oficina.dto.FuncionarioPublicDto;
import miguel.projetos.oficina.entity.Funcionario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import miguel.projetos.oficina.service.FuncionarioService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    // --- LISTAR FUNCIONÁRIOS ---
    @GetMapping
    @PreAuthorize("hasAnyAuthority('GERENTE','FUNCIONARIO')")
    public ResponseEntity<List<?>> getAll(Authentication auth) {
        List<Funcionario> funcionarios = funcionarioService.findAll();

        boolean isGerente = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("GERENTE"));

        if (isGerente) {
            List<FuncionarioDto> dtos = funcionarios.stream()
                    .map(this::convertToDto)
                    .toList();
            return ResponseEntity.ok(dtos);
        } else {
            List<FuncionarioPublicDto> publicDtos = funcionarios.stream()
                    .map(this::convertToPublicDto)
                    .toList();
            return ResponseEntity.ok(publicDtos);
        }
    }

    // --- CRIAR FUNCIONÁRIO (somente gerente) ---
    @PostMapping
    @PreAuthorize("hasAuthority('GERENTE')")
    public ResponseEntity<FuncionarioDto> create(@Valid @RequestBody FuncionarioCreateDto createDto) {
        Funcionario funcionario = convertCreateDtoToEntity(createDto);
        Funcionario novoFuncionario = funcionarioService.save(funcionario);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(novoFuncionario));
    }

    // --- DELETAR FUNCIONÁRIO (somente gerente) ---
    @DeleteMapping("/cpf/{cpf}")
    @PreAuthorize("hasAuthority('GERENTE')")
    public ResponseEntity<Void> delete(@PathVariable String cpf) {
        funcionarioService.delete(cpf);
        return ResponseEntity.noContent().build();
    }

    // --- GERENTE OU O PRÓPRIO FUNCIONÁRIO ---
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("#cpf == authentication.name or hasAuthority('GERENTE')")

    public ResponseEntity<FuncionarioDto> getByCpf(@PathVariable String cpf) {
        Funcionario funcionario = funcionarioService.findByCpf(cpf)
                .orElseThrow(() -> new NoSuchElementException("Funcionário com CPF " + cpf + " não encontrado."));
        return ResponseEntity.ok(convertToDto(funcionario));
    }

    @PutMapping("/cpf/{cpf}")
    @PreAuthorize("#cpf == authentication.name or hasAuthority('GERENTE')")

    public ResponseEntity<FuncionarioDto> update(@PathVariable String cpf, @Valid @RequestBody FuncionarioDto updateDto) {
        Funcionario funcionario = convertToEntity(updateDto);
        Funcionario funcionarioAtualizado = funcionarioService.update(cpf, funcionario);
        return ResponseEntity.ok(convertToDto(funcionarioAtualizado));
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

   private FuncionarioPublicDto convertToPublicDto(Funcionario funcionario) {
    FuncionarioPublicDto dto = new FuncionarioPublicDto();
    dto.setId_usuario(funcionario.getId_usuario()); // garante que o front receba
    dto.setNome(funcionario.getNome());
    dto.setCargo(funcionario.getCargo());
    dto.setTelefone(maskTelefone(funcionario.getTelefone()));
    return dto;
}

    private String maskTelefone(String tel) {
        if (tel == null || tel.length() < 4) return "****";
        return "****" + tel.substring(tel.length() - 4);
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
        return funcionario;
    }

    private Funcionario convertCreateDtoToEntity(FuncionarioCreateDto createDto) {
        Funcionario funcionario = new Funcionario();
        funcionario.setCpf(createDto.getCpf());
        funcionario.setNome(createDto.getNome());
        funcionario.setSenha(createDto.getSenha()); // senha só no create
        funcionario.setCargo(createDto.getCargo());
        funcionario.setTelefone(createDto.getTelefone());
        funcionario.setEndereco(createDto.getEndereco());
        funcionario.setEmail(createDto.getEmail());
        return funcionario;
    }
}
