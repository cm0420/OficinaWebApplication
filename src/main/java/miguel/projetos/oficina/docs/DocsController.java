// src/main/java/miguel/projetos/oficina/docs/DocsController.java
package miguel.projetos.oficina.docs;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class DocsController {

    private final DocsMarkdownService markdownService;

    public DocsController(DocsMarkdownService markdownService) {
        this.markdownService = markdownService;
    }

    // já existente (um único MD “geral”, se quiser manter)
    @GetMapping("/docs/download-md")
    public ResponseEntity<Resource> downloadSingle() {
        try {
            Path openapiFile = Path.of("docs/openapi.json");
            // Reaproveite se quiser montar um "geral.md" a partir do map
            String all = String.join("\n\n---\n\n",
                    markdownService.generateMarkdownByTag(openapiFile).values());
            byte[] bytes = all.getBytes();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=api-docs.md")
                    .contentType(MediaType.parseMediaType("text/markdown"))
                    .contentLength(bytes.length)
                    .body(new ByteArrayResource(bytes));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(new ByteArrayResource(("Erro: " + e.getMessage()).getBytes()));
        }
    }

    // 👉 novo: zip com “um .md por controller (tag)” + 00-schemas.md
    @GetMapping("/docs/download-md-all")
    public ResponseEntity<Resource> downloadAllZip() {
        try {
            Path openapiFile = Path.of("docs/openapi.json");
            Map<String, String> files = markdownService.generateMarkdownByTag(openapiFile);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                for (Map.Entry<String, String> e : files.entrySet()) {
                    zos.putNextEntry(new ZipEntry(e.getKey()));
                    zos.write(e.getValue().getBytes());
                    zos.closeEntry();
                }
            }

            byte[] zipBytes = baos.toByteArray();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=api-docs-md.zip")
                    .contentType(MediaType.parseMediaType("application/zip"))
                    .contentLength(zipBytes.length)
                    .body(new ByteArrayResource(zipBytes));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(new ByteArrayResource(("Erro: " + e.getMessage()).getBytes()));
        }
    }
}
