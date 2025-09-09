// src/main/java/miguel/projetos/oficina/docs/DocsMarkdownService.java
package miguel.projetos.oficina.docs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;

@Service
public class DocsMarkdownService {

    private final ObjectMapper mapper = new ObjectMapper();

    public String generateMarkdownFromOpenApi(Path openapiPath) throws Exception {
        if (!Files.exists(openapiPath)) {
            throw new IllegalStateException("OpenAPI file not found: " + openapiPath.toAbsolutePath());
        }

        JsonNode root = mapper.readTree(Files.readString(openapiPath));

        StringBuilder md = new StringBuilder();
        md.append("# 📄 Documentação da API\n\n");

        // Info
        JsonNode info = root.path("info");
        String title = info.path("title").asText("API");
        String version = info.path("version").asText("");
        String description = info.path("description").asText("");
        md.append("**").append(title).append("**");
        if (!version.isBlank()) md.append(" — v").append(version);
        md.append("\n\n");
        if (!description.isBlank()) md.append(description).append("\n\n");

        // Servers
        JsonNode servers = root.path("servers");
        if (servers.isArray() && servers.size() > 0) {
            md.append("## 🌐 Servidores\n\n");
            for (JsonNode s : servers) {
                md.append("- `").append(s.path("url").asText()).append("`");
                String desc = s.path("description").asText("");
                if (!desc.isBlank()) md.append(" — ").append(desc);
                md.append("\n");
            }
            md.append("\n");
        }

        // Paths
        JsonNode paths = root.path("paths");
        if (paths.isObject()) {
            md.append("## 🔀 Endpoints\n\n");
            Iterator<Map.Entry<String, JsonNode>> it = paths.fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> entry = it.next();
                String path = entry.getKey();
                JsonNode methods = entry.getValue();

                Iterator<Map.Entry<String, JsonNode>> mit = methods.fields();
                while (mit.hasNext()) {
                    Map.Entry<String, JsonNode> mEntry = mit.next();
                    String method = mEntry.getKey().toUpperCase();
                    JsonNode op = mEntry.getValue();

                    String opId = op.path("operationId").asText("");
                    md.append("### `").append(method).append(" ").append(path).append("`\n\n");
                    if (!opId.isBlank()) md.append("- **operationId:** `").append(opId).append("`\n");

                    // Tags
                    if (op.has("tags") && op.get("tags").isArray()) {
                        md.append("- **tags:** ");
                        for (int i = 0; i < op.get("tags").size(); i++) {
                            if (i > 0) md.append(", ");
                            md.append("`").append(op.get("tags").get(i).asText()).append("`");
                        }
                        md.append("\n");
                    }

                    // Parameters
                    if (op.has("parameters") && op.get("parameters").isArray() && op.get("parameters").size() > 0) {
                        md.append("\n**Parâmetros**\n\n");
                        md.append("| Nome | In | Tipo | Obrigatório |\n");
                        md.append("|---|---|---|---|\n");
                        for (JsonNode p : op.get("parameters")) {
                            String name = p.path("name").asText();
                            String in = p.path("in").asText();
                            String type = p.path("schema").path("type").asText("-");
                            String required = p.path("required").asBoolean(false) ? "sim" : "não";
                            md.append("| `").append(name).append("` | ").append(in).append(" | ")
                              .append(type).append(" | ").append(required).append(" |\n");
                        }
                        md.append("\n");
                    }

                    // Request body
                    if (op.has("requestBody")) {
                        md.append("**Request Body**\n\n");
                        JsonNode rb = op.get("requestBody");
                        JsonNode content = rb.path("content");
                        if (content.isObject()) {
                            Iterator<String> mediaTypes = content.fieldNames();
                            while (mediaTypes.hasNext()) {
                                String mt = mediaTypes.next();
                                JsonNode schema = content.path(mt).path("schema");
                                md.append("- `").append(mt).append("` → ");
                                md.append(schemaSummary(schema)).append("\n");
                            }
                        }
                        md.append("\n");
                    }

                    // Responses
                    if (op.has("responses")) {
                        md.append("**Responses**\n\n");
                        md.append("| Código | Schema |\n|---|---|\n");
                        Iterator<Map.Entry<String, JsonNode>> rIt = op.get("responses").fields();
                        while (rIt.hasNext()) {
                            Map.Entry<String, JsonNode> r = rIt.next();
                            String code = r.getKey();
                            JsonNode rNode = r.getValue();
                            String schemaStr = "-";
                            JsonNode rContent = rNode.path("content");
                            if (rContent.isObject()) {
                                Iterator<String> rMts = rContent.fieldNames();
                                if (rMts.hasNext()) {
                                    String mt = rMts.next();
                                    schemaStr = "`" + mt + "` → " + schemaSummary(rContent.path(mt).path("schema"));
                                }
                            }
                            md.append("| ").append(code).append(" | ").append(schemaStr).append(" |\n");
                        }
                        md.append("\n");
                    }

                    md.append("\n");
                }
            }
        }

        // Schemas
        JsonNode components = root.path("components");
        JsonNode schemas = components.path("schemas");
        if (schemas.isObject() && schemas.size() > 0) {
            md.append("## 🧩 Schemas\n\n");
            Iterator<Map.Entry<String, JsonNode>> sit = schemas.fields();
            while (sit.hasNext()) {
                Map.Entry<String, JsonNode> s = sit.next();
                String name = s.getKey();
                JsonNode schema = s.getValue();
                md.append("### ").append(name).append("\n\n");
                // properties
                JsonNode props = schema.path("properties");
                if (props.isObject() && props.size() > 0) {
                    md.append("| Propriedade | Tipo |\n|---|---|\n");
                    Iterator<Map.Entry<String, JsonNode>> pit = props.fields();
                    while (pit.hasNext()) {
                        Map.Entry<String, JsonNode> p = pit.next();
                        String pName = p.getKey();
                        String pType = schemaSummary(p.getValue());
                        md.append("| `").append(pName).append("` | ").append(pType).append(" |\n");
                    }
                    md.append("\n");
                }
                md.append("\n");
            }
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
        if (type.equals("array")) {
            return "array<" + schemaSummary(schema.path("items")) + ">";
        }
        if (schema.has("format")) {
            return type + " (" + schema.get("format").asText() + ")";
        }
        return type;
    }
}
