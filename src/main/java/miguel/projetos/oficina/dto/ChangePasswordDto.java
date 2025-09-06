// src/main/java/miguel/projetos/oficina/dto/ChangePasswordDto.java
package miguel.projetos.oficina.dto;

import jakarta.validation.constraints.NotBlank;

public class ChangePasswordDto {
    @NotBlank
    private String senhaAtual;

    @NotBlank
    private String novaSenha;

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }
}
