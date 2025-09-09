// src/main/java/miguel/projetos/oficina/docs/DocsGenerator.java
package miguel.projetos.oficina.docs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class DocsGenerator {

  private final RestTemplate http = new RestTemplate();
  private final ServletWebServerApplicationContext web;

  // Habilita/desabilita por propriedade
  @Value("${docs.generation.enabled:true}")
  private boolean enabled;

  // Se quiser forçar uma base externa, defina DOCS_BASE_URL; senão, usa localhost:porta
  @Value("${docs.base-url:}")
  private String baseUrlOverride;

  public DocsGenerator(ServletWebServerApplicationContext web) {
    this.web = web;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void generate() {
    if (!enabled) {
      System.out.println("📄 Geração de docs desabilitada (docs.generation.enabled=false).");
      return;
    }
    try {
      int port = web.getWebServer().getPort();
      String base =  "http://localhost:" + port;

      String url = base + "/v3/api-docs";
      System.out.println("📄 Gerando documentação OpenAPI a partir de: " + url);

      String body = http.getForObject(url, String.class);

      Path outDir = Path.of("docs");
      Files.createDirectories(outDir);
      Files.writeString(outDir.resolve("openapi.json"), body);

      System.out.println("✅ OpenAPI salvo em docs/openapi.json");
    } catch (Exception e) {
      System.err.println("⚠️ Falha ao gerar docs (app continua): " + e.getMessage());
    }
  }
}
