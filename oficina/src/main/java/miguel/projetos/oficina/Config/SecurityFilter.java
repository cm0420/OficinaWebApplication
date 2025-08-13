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

    // rotas públicas + docs + actuator
    private static final List<String> SKIP_URLS = List.of(
        "/auth/login",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/swagger-resources/**",
        "/webjars/**",
        "/actuator/**",
        "/error" // útil quando dá exception handler
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 1) nunca filtra preflight
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        // 2) nem as rotas públicas
        String path = request.getRequestURI();
        return SKIP_URLS.stream().anyMatch(p -> matcher.match(p, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // a esta altura não é OPTIONS (já teria saído no shouldNotFilter)
        String token = recoverToken(request);

        if (token != null) {
            try {
                String subject = tokenService.validateToken(token); // esperado: CPF
                if (subject != null) {
                    String cpf = subject.replaceAll("\\D", ""); // só dígitos

                    UserDetails user = funcionarioRepository
                        .findFuncionarioByCpf(cpf)
                        .orElse(null);

                    if (user != null) {
                        var auth = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities()
                        );
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (Exception e) {
                // token inválido/expirado -> segue sem autenticar
                // (não envia erro aqui; deixa o Security decidir nas rules)
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
