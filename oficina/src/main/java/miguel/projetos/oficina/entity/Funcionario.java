package miguel.projetos.oficina.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "funcionarios")
@Data
@NoArgsConstructor
public class Funcionario implements UserDetails {
    @Id
    private String id_usuario;

    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String senha;

    private String cargo;
    private String telefone;
    private String endereco;
    private String email;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorna o cargo do funcionário como uma permissão para o Spring Security
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.cargo.toUpperCase()));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        // No nosso sistema, o username é o CPF
        return this.cpf;
    }

    // Para simplificar, vamos retornar 'true' para estes métodos.
    // Eles controlam se a conta está expirada, bloqueada, etc.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
