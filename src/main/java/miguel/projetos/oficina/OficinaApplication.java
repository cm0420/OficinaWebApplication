package miguel.projetos.oficina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class OficinaApplication {

    public static void main(String[] args) {
        // Carrega o .env antes de iniciar o Spring
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        // Seta todas as variáveis do .env como propriedades do sistema
        dotenv.entries().forEach(entry -> {
            String key = entry.getKey();
            String value = entry.getValue();
            System.setProperty(key, value);
        });

        // Inicia o Spring Boot
        SpringApplication.run(OficinaApplication.class, args);
    }
}
