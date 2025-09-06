package miguel.projetos.oficina.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class RoleNormalizer {

    public static String normalize(String cargo) {
        if (cargo == null) {
            return "FUNCIONARIO";
        }

        String upper = cargo.trim().toUpperCase();

        if (upper.contains("GERENTE") || upper.equals("ADMIN")) {
            return "GERENTE";
        }

        return "FUNCIONARIO";
    }

    public static GrantedAuthority asAuthority(String cargo) {
        return new SimpleGrantedAuthority(normalize(cargo));
    }
}
