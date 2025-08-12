package miguel.projetos.oficina.Config;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import miguel.projetos.oficina.Repository.FuncionarioRepository;
import miguel.projetos.oficina.service.TokenService;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    FuncionarioRepository funcionarioRepository;

    private static final AntPathMatcher matcher = new AntPathMatcher();
    private static final List<String> SKIP_URLS = List.of(
        "/auth/login",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/swagger-resources/**",
        "/webjars/**",
        "/actuator/**"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return SKIP_URLS.stream().anyMatch(p -> matcher.match(p, path));
    }

    @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

    // CORS preflight: não tenta autenticar
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
        chain.doFilter(request, response);
        return;
    }

    String token = recoverToken(request);
    if (token != null) {
        try {
            String subject = tokenService.validateToken(token); // deve ser o CPF
            if (subject != null) {
                // ⚠️ garante só dígitos
                String cpf = subject.replaceAll("\\D", "");

                UserDetails user = funcionarioRepository.findFuncionarioByCpf(cpf).orElse(null);
                if (user != null) {
                    var auth = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities() // precisa conter ROLE_GERENTE
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception e) {
            // token inválido/expirado -> segue sem autenticar
        }
    }

    chain.doFilter(request, response);
}


    private String recoverToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) return null;
        return auth.substring(7);
    }
}
