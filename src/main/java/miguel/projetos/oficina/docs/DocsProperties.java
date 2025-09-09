package miguel.projetos.oficina.docs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "docs")
public class DocsProperties {
    private boolean generate = false;
    private String outputDir = "./docs";
    private String baseUrl = "http://localhost:8001";
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
