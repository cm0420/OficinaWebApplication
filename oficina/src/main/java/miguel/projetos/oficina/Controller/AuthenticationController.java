package miguel.projetos.oficina.Controller;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import miguel.projetos.oficina.Repository.FuncionarioRepository;
import miguel.projetos.oficina.dto.ChangePasswordDto;
import miguel.projetos.oficina.dto.LoginRequestDto;
import miguel.projetos.oficina.dto.LoginResponseDto;
import miguel.projetos.oficina.entity.Funcionario;
import miguel.projetos.oficina.service.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        // Se seu principal é a entidade Funcionario, troque o tipo acima por Funcionario
        // e devolva os campos que quiser (cpf/nome/cargo).
        String cpf = user.getUsername(); // deve ser o CPF que veio no token
        var opt = funcionarioRepository.findFuncionarioByCpf(cpf);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        var f = opt.get();
        var body = Map.of(
            "cpf", f.getCpf(),
            "nome", f.getNome(),
            "cargo", f.getCargo()
        );
        return ResponseEntity.ok(body);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto data) {
        // Se a autenticação falhar, o Spring lança AuthenticationException.
        // O RestExceptionHandler irá capturar e retornar uma resposta 401 padronizada.
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getCpf(), data.getSenha());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((Funcionario) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/changepassword")
    public ResponseEntity<String> mudarSenha(
            @RequestBody @Valid ChangePasswordDto data,
            @AuthenticationPrincipal Funcionario funcionarioLogado
    ) {
        // Validação da senha
        if (!passwordEncoder.matches(data.getSenhaAtual(), funcionarioLogado.getSenha())) {
            // Lança uma exceção em vez de retornar uma resposta manual.
            // O RestExceptionHandler irá capturar e retornar um erro 400 padronizado.
            throw new IllegalArgumentException("A senha atual fornecida está incorreta.");
        }

        // Lógica de negócio permanece a mesma
        funcionarioLogado.setSenha(passwordEncoder.encode(data.getNovaSenha()));
        funcionarioRepository.save(funcionarioLogado);

        return ResponseEntity.ok("Senha alterada com sucesso.");
    }
}
