package miguel.projetos.oficina.service;
import miguel.projetos.oficina.Repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;
@Service
public class AuthorrizationService implements UserDetailsService {
    @Autowired
    FuncionarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Agora, simplesmente encontramos o funcionário e o retornamos,
        // pois ele já é um UserDetails.
        return repository.findFuncionarioByCpf(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado com o CPF: " + username));
    }
}
