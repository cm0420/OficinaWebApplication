package miguel.projetos.oficina.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequestDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getCpf(), data.getSenha());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((Funcionario) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/changepassword")
    public ResponseEntity<?> mudarSenha(
            @RequestBody @Valid ChangePasswordDto data,
            @AuthenticationPrincipal Funcionario funcionarioLogado
    ) {
        if (!passwordEncoder.matches(data.getSenhaAtual(), funcionarioLogado.getSenha())) {
            return ResponseEntity.status(403).body("Senha atual incorreta.");
        }

        funcionarioLogado.setSenha(passwordEncoder.encode(data.getNovaSenha()));
        funcionarioRepository.save(funcionarioLogado);
        return ResponseEntity.ok("Senha alterada com sucesso.");
    }
}
