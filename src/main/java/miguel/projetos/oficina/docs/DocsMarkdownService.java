// src/main/java/miguel/projetos/oficina/docs/DocsMarkdownService.java
package miguel.projetos.oficina.docs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class DocsMarkdownService {
    private final ObjectMapper mapper = new ObjectMapper();

    public Map<String, String> generateMarkdownByTag(Path openapiPath) throws Exception {
        if (!Files.exists(openapiPath)) {
            throw new IllegalStateException("OpenAPI file not found: " + openapiPath.toAbsolutePath());
        }
        JsonNode root = mapper.readTree(Files.readString(openapiPath));

        // metadata
        String apiTitle = root.path("info").path("title").asText("API");
        String apiVersion = root.path("info").path("version").asText("");
        String apiDesc = root.path("info").path("description").asText("");

        // agrupa operações por tag
        JsonNode paths = root.path("paths");
        Map<String, List<EndpointOp>> byTag = new LinkedHashMap<>();
        paths.fields().forEachRemaining(pe -> {
            String path = pe.getKey();
            pe.getValue().fields().forEachRemaining(me -> {
                String method = me.getKey().toUpperCase(Locale.ROOT);
                JsonNode op = me.getValue();
                if (!op.has("tags")) {
                    byTag.computeIfAbsent("untagged", k -> new ArrayList<>())
                        .add(new EndpointOp(path, method, op));
                    return;
                }
                for (JsonNode t : op.get("tags")) {
                    String tag = t.asText("untagged");
                    byTag.computeIfAbsent(tag, k -> new ArrayList<>())
                        .add(new EndpointOp(path, method, op));
                }
            });
        });

        // schemas globais (um arquivo separado)
        String schemasMd = buildSchemas(root.path("components").path("schemas"));

        Map<String, String> result = new LinkedHashMap<>();
        for (Map.Entry<String, List<EndpointOp>> e : byTag.entrySet()) {
            String tag = e.getKey();
            StringBuilder md = new StringBuilder();
            md.append("# ").append(apiTitle);
            if (!apiVersion.isBlank()) md.append(" — v").append(apiVersion);
            md.append("\n\n");
            if (!apiDesc.isBlank()) md.append(apiDesc).append("\n\n");
            md.append("## Controller: ").append(tag).append("\n\n");

            for (EndpointOp op : e.getValue()) {
                appendEndpoint(md, op);
            }

            result.put(safeFilename(tag) + ".md", md.toString());
        }

        // adiciona schemas como “00-schemas.md” (opcional: comentar esta linha se quiser schemas em cada arquivo)
        result.put("00-schemas.md", schemasMd);

        return result;
    }

    private void appendEndpoint(StringBuilder md, EndpointOp op) {
        String opId = op.node.path("operationId").asText("");
        md.append("### `").append(op.method).append(" ").append(op.path).append("`\n\n");
        if (!opId.isBlank()) md.append("- **operationId:** `").append(opId).append("`\n");

        // parâmetros
        JsonNode params = op.node.path("parameters");
        if (params.isArray() && params.size() > 0) {
            md.append("\n**Parâmetros**\n\n");
            md.append("| Nome | In | Tipo | Obrigatório |\n|---|---|---|---|\n");
            for (JsonNode p : params) {
                String name = p.path("name").asText("-");
                String in = p.path("in").asText("-");
                String type = p.path("schema").path("type").asText("-");
                String required = p.path("required").asBoolean(false) ? "sim" : "não";
                md.append("| `").append(name).append("` | ").append(in)
                  .append(" | ").append(type).append(" | ").append(required).append(" |\n");
            }
            md.append("\n");
        }

        // body
        JsonNode rb = op.node.path("requestBody");
        if (!rb.isMissingNode()) {
            md.append("**Request Body**\n\n");
            JsonNode content = rb.path("content");
            if (content.isObject()) {
                Iterator<String> mts = content.fieldNames();
                while (mts.hasNext()) {
                    String mt = mts.next();
                    JsonNode schema = content.path(mt).path("schema");
                    md.append("- `").append(mt).append("` → ").append(schemaSummary(schema)).append("\n");
                }
            }
            md.append("\n");
        }

        // responses
        JsonNode resp = op.node.path("responses");
        if (resp.isObject()) {
            md.append("**Responses**\n\n| Código | Schema |\n|---|---|\n");
            resp.fields().forEachRemaining(r -> {
                String code = r.getKey();
                String schemaStr = "-";
                JsonNode content = r.getValue().path("content");
                if (content.isObject()) {
                    Iterator<String> mts = content.fieldNames();
                    if (mts.hasNext()) {
                        String mt = mts.next();
                        schemaStr = "`" + mt + "` → " + schemaSummary(content.path(mt).path("schema"));
                    }
                }
                md.append("| ").append(code).append(" | ").append(schemaStr).append(" |\n");
            });
            md.append("\n");
        }

        md.append("\n");
    }

    private String buildSchemas(JsonNode schemas) {
        StringBuilder md = new StringBuilder("# 🧩 Schemas\n\n");
        if (schemas.isObject()) {
            schemas.fields().forEachRemaining(e -> {
                md.append("## ").append(e.getKey()).append("\n\n");
                JsonNode schema = e.getValue();
                JsonNode props = schema.path("properties");
                if (props.isObject() && props.size() > 0) {
                    md.append("| Propriedade | Tipo |\n|---|---|\n");
                    props.fields().forEachRemaining(p -> {
                        md.append("| `").append(p.getKey()).append("` | ")
                          .append(schemaSummary(p.getValue())).append(" |\n");
                    });
                    md.append("\n");
                }
            });
        }
        return md.toString();
    }

    private String schemaSummary(JsonNode schema) {
        if (schema == null || schema.isMissingNode()) return "-";
        if (schema.has("$ref")) {
            String ref = schema.get("$ref").asText();
            return "ref → `" + ref.replace("#/components/schemas/", "") + "`";
        }
        String type = schema.path("type").asText("-");
        if ("array".equals(type)) {
            return "array<" + schemaSummary(schema.path("items")) + ">";
        }
        if (schema.has("format")) {
            return type + " (" + schema.get("format").asText() + ")";
        }
        return type;
    }

    private String safeFilename(String s) {
        return s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9-_]+", "-");
    }

    private record EndpointOp(String path, String method, JsonNode node) {}
}
