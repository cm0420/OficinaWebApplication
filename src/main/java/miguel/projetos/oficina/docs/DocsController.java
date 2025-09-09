// src/main/java/miguel/projetos/oficina/docs/DocsController.java
package miguel.projetos.oficina.docs;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class DocsController {

    @GetMapping("/docs/download-md")
    public ResponseEntity<Resource> downloadDocs() {
        try {
            Path openapiFile = Path.of("docs/openapi.json");
            if (!Files.exists(openapiFile)) {
                return ResponseEntity.notFound().build();
            }

            String json = Files.readString(openapiFile);

            // Aqui você pode usar uma lib tipo swagger2markup, mas vamos fazer simples:
            String markdown = """
                # 📄 Documentação da API

                Este arquivo foi gerado automaticamente a partir do OpenAPI.

                ```json
                %s
                ```
                """.formatted(json);

            byte[] bytes = markdown.getBytes();
            Resource resource = new ByteArrayResource(bytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=api-docs.md")
                    .contentType(MediaType.parseMediaType("text/markdown"))
                    .contentLength(bytes.length)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ByteArrayResource(("Erro: " + e.getMessage()).getBytes()));
        }
    }
}
