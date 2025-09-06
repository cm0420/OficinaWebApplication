package miguel.projetos.oficina.Config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(c -> {}) // usa o bean CorsConfigurationSource abaixo
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // SecurityConfig.java (trecho do authorizeHttpRequests)
.authorizeHttpRequests(auth -> auth
    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
    .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
    .requestMatchers("/actuator/health", "/actuator/info").permitAll()
    .requestMatchers(
        "/swagger-ui.html","/swagger-ui/**",
        "/v3/api-docs","/v3/api-docs/**",
        "/swagger-resources","/swagger-resources/**",
        "/webjars/**"
    ).permitAll()

    // auth básica
    .requestMatchers(HttpMethod.GET, "/auth/me").authenticated()

    // ======= REGRAS AJUSTADAS =======
    // Financeiro: só gerente
    .requestMatchers("/api/financeiro/**").hasAuthority("GERENTE")

    // Funcionários:
    .requestMatchers(HttpMethod.GET, "/api/funcionarios").hasAnyAuthority("GERENTE","FUNCIONARIO")
    .requestMatchers(HttpMethod.POST, "/api/funcionarios").hasAuthority("GERENTE")
    .requestMatchers(HttpMethod.DELETE, "/api/funcionarios/**").hasAuthority("GERENTE")

    // “Eu mesmo” por CPF: qualquer autenticado (o @PreAuthorize do controller já restringe ao próprio CPF ou GERENTE)
    .requestMatchers(HttpMethod.GET, "/api/funcionarios/cpf/**").authenticated()
    .requestMatchers(HttpMethod.PUT, "/api/funcionarios/cpf/**").authenticated()
    // ================================

    .anyRequest().authenticated()
)

            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Value("${frontend.origin}")
    private String frontendOrigin;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();

        // Suporta várias origens (separadas por vírgula no env)
        List<String> origins = Arrays.stream(frontendOrigin.split(","))
                                    .map(String::trim)
                                    .toList();

        cfg.setAllowedOrigins(origins);

        cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization","Content-Type","Accept","Origin","X-Requested-With"));
        cfg.setExposedHeaders(List.of("Authorization","Location"));
        cfg.setAllowCredentials(true);
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

}
