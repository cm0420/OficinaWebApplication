package miguel.projetos.oficina.docs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "docs")
public class DocsProperties {
    /** gerar ou não na inicialização */
    private boolean generate = true;
    /** diretório de saída (relativo ao working dir do container) */
    private String outputDir = "./docs";
    /** base-url da API para puxar /v3/api-docs */
    private String baseUrl = "http://localhost:8001";
    /** se deve gerar 1 arquivo por rota */
    private boolean splitPerRoute = true;

    // getters e setters
    public boolean isGenerate() { return generate; }
    public void setGenerate(boolean generate) { this.generate = generate; }
    public String getOutputDir() { return outputDir; }
    public void setOutputDir(String outputDir) { this.outputDir = outputDir; }
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public boolean isSplitPerRoute() { return splitPerRoute; }
    public void setSplitPerRoute(boolean splitPerRoute) { this.splitPerRoute = splitPerRoute; }
}
