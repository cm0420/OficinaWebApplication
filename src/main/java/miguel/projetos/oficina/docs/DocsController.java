// src/main/java/miguel/projetos/oficina/docs/DocsController.java
package miguel.projetos.oficina.docs;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
public class DocsController {

    private final DocsMarkdownService markdownService;

    public DocsController(DocsMarkdownService markdownService) {
        this.markdownService = markdownService;
    }

    @GetMapping("/docs/download-md")
    public ResponseEntity<Resource> downloadDocs() {
        try {
            Path openapiFile = Path.of("docs/openapi.json");
            String markdown = markdownService.generateMarkdownFromOpenApi(openapiFile);
            byte[] bytes = markdown.getBytes();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=api-docs.md")
                    .contentType(MediaType.parseMediaType("text/markdown"))
                    .contentLength(bytes.length)
                    .body(new ByteArrayResource(bytes));

        } catch (Exception e) {
            byte[] err = ("Erro gerando Markdown: " + e.getMessage()).getBytes();
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(new ByteArrayResource(err));
        }
    }
}
