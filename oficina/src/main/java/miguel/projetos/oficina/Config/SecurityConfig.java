package miguel.projetos.oficina.Config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

import jakarta.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    /**
     * Env (ou application.properties):
     * FRONTEND_ORIGIN=https://oficina.flipafile.com,https://*.flipafile.com,http://localhost:3000
     *
     * Obs: com allowCredentials(true) NÃO use "*" literal.
     */
    @Value("${frontend.origin}")
    private String frontendOrigins;

    @PostConstruct
    void logCors() {
        System.out.println("[CORS] frontend.origin=" + frontendOrigins);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(c -> c.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // libera somente o preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .requestMatchers(
                    "/swagger-ui.html", "/swagger-ui/**",
                    "/v3/api-docs", "/v3/api-docs/**",
                    "/swagger-resources", "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()

                .requestMatchers(HttpMethod.GET, "/auth/me").authenticated()
                .requestMatchers("/api/financeiro/**", "/api/funcionarios/**").hasRole("GERENTE")
                .anyRequest().authenticated()
            )
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();

        // limpa entradas vazias e espaços
        List<String> origins = Arrays.stream(frontendOrigins.split(","))
            .map(String::trim)
            .filter(s -> !s.isBlank())
            .toList();

        // Use patterns para aceitar curingas como https://*.flipafile.com
        cfg.setAllowedOriginPatterns(origins);

        // métodos de app + OPTIONS para o preflight
        cfg.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","PATCH","OPTIONS"));

        // só cabeçalhos usuais; ajuste se precisar de mais
        cfg.setAllowedHeaders(Arrays.asList(
            "Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"
        ));

        // o front costuma ler Authorization/Location
        cfg.setExposedHeaders(Arrays.asList("Authorization", "Location"));

        // necessário se usa cookie/JWT em header com Origin específico
        cfg.setAllowCredentials(true);

        // cache do preflight no browser
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
