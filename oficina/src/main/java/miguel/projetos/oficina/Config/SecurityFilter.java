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

        String token = recoverToken(request);
        if (token != null) {
            try {
                String subject = tokenService.validateToken(token); // não pode lançar pra fora
                if (subject != null) {
                    UserDetails user = funcionarioRepository.findFuncionarioByCpf(subject).orElse(null);
                    if (user != null) {
                        var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (Exception e) {
                // Token inválido/expirado/assinado errado: não autentica e segue o fluxo
                // (deixa a autorização barrar depois, sem derrubar com 500)
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
