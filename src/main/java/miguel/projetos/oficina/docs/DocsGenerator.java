package miguel.projetos.oficina.docs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Component
public class DocsGenerator {

    private final DocsProperties props;
    private final ObjectMapper mapper = new ObjectMapper();

    public DocsGenerator(DocsProperties props) {
        this.props = props;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void generate() throws IOException {
        if (!props.isGenerate()) return;

        System.out.println("📄 Gerando documentação Markdown da API...");
        RestTemplate rt = new RestTemplate();
        String spec = rt.getForObject(props.getBaseUrl() + "/v3/api-docs", String.class);

        JsonNode root = mapper.readTree(spec);
        Path outDir = Paths.get(props.getOutputDir());
        Files.createDirectories(outDir);

        // arquivo principal
        Path readme = outDir.resolve("README.md");
        Files.writeString(readme, "# Oficina API — Documentação\n\nBase URL: " + props.getBaseUrl() + "\n");

        if (props.isSplitPerRoute()) {
            Path routesDir = outDir.resolve("routes");
            Files.createDirectories(routesDir);

            List<String> indexLinks = new ArrayList<>();
            root.get("paths").fields().forEachRemaining(entry -> {
                String path = entry.getKey();
                JsonNode methods = entry.getValue();
                String fileName = path.replace("/", "_").replace("{", "[").replace("}", "]") + ".md";
                Path f = routesDir.resolve(fileName);

                StringBuilder sb = new StringBuilder();
                sb.append("# ").append(path).append("\n\n");
                methods.fields().forEachRemaining(m -> {
                    sb.append("## ").append(m.getKey().toUpperCase()).append("\n");
                    sb.append("```\n");
                    sb.append("curl -X ").append(m.getKey().toUpperCase()).append(" ")
                      .append(props.getBaseUrl()).append(path).append("\n```\n\n");
                });

                try {
                    Files.writeString(f, sb.toString());
                    indexLinks.add("- [" + path + "](./routes/" + fileName + ")");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            // adiciona sumário no README
            Files.writeString(readme,
                    Files.readString(readme) + "\n## Rotas\n\n" + String.join("\n", indexLinks) + "\n");
        }

        System.out.println("✅ Documentação gerada em " + outDir.toAbsolutePath());
    }
}
