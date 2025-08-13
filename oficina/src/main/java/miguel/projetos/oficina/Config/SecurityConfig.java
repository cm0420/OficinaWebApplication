package miguel.projetos.oficina.Config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import jakarta.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    @Value("${frontend.origin}")
    private String frontendOrigins;

    @PostConstruct
    void logCors() { System.out.println("[CORS] frontend.origin=" + frontendOrigins); }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(c -> {}) // cors vem do CorsFilter abaixo (order=0)
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // libere SOMENTE preflight:
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .requestMatchers(
                    "/swagger-ui.html","/swagger-ui/**",
                    "/v3/api-docs","/v3/api-docs/**",
                    "/swagger-resources","/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()

                .requestMatchers(HttpMethod.GET, "/auth/me").authenticated()
                .requestMatchers("/api/financeiro/**", "/api/funcionarios/**").hasRole("GERENTE")
                .anyRequest().authenticated()
            )
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    // CORS no topo da cadeia
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterReg() {
        var cfg = new CorsConfiguration();

        List<String> origins = Arrays.stream(frontendOrigins.split(","))
            .map(String::trim).filter(s -> !s.isBlank()).toList();

        // aceita padrões como https://*.flipafile.com e entradas exatas
        cfg.setAllowedOriginPatterns(origins);

        cfg.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        cfg.setAllowedHeaders(Arrays.asList(
            "Authorization","Content-Type","Accept","Origin","X-Requested-With"
        ));
        cfg.setExposedHeaders(Arrays.asList("Authorization","Location"));
        cfg.setAllowCredentials(true);
        cfg.setMaxAge(3600L);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);

        var bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(0); // garante que roda antes do seu SecurityFilter
        return bean;
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
