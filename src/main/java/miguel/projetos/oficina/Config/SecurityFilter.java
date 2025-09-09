package miguel.projetos.oficina.Config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import miguel.projetos.oficina.Repository.FuncionarioRepository;
import miguel.projetos.oficina.service.TokenService;
import miguel.projetos.oficina.util.RoleNormalizer;

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
        "/docs/download-md",
        "/error"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;
        String path = request.getRequestURI();
        return SKIP_URLS.stream().anyMatch(p -> matcher.match(p, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

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
                        // Normaliza as roles vindas do banco
                        var normalizedAuthorities = user.getAuthorities().stream()
                            .map(a -> RoleNormalizer.normalize(a.getAuthority()))
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                        var authentication = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            normalizedAuthorities
                        );
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                // token inválido/expirado -> segue sem autenticar
            }
        }

        chain.doFilter(request, response);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    private String recoverToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) return null;
        return auth.substring(7);
    }
}
