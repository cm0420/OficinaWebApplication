package miguel.projetos.oficina.Config;


import org.springframework.beans.factory.annotation.Autowired;
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
// CORREÇÃO: Importações corretas para Spring MVC (Servlet)
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // <-- ADICIONE ESTA LINHA
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers("/api/financeiro/**", "/api/funcionarios/**").hasRole("GERENTE")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite que o seu front-end acesse a API
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        // Permite todos os métodos HTTP (GET, POST, PUT, DELETE)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Permite todos os cabeçalhos, incluindo o de Authorization
        configuration.setAllowedHeaders(List.of("*"));
        // Permite o envio de credenciais (cookies, etc., se necessário)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuração a todos os caminhos da sua API
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    /**
     * Disponibiliza o AuthenticationManager como um Bean para que possa ser injetado
     * no nosso AuthenticationController.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Define o BCrypt como o algoritmo para encriptar e verificar senhas.
     * O Spring Security irá usá-lo automaticamente quando o AuthenticationManager
     * for chamado.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
