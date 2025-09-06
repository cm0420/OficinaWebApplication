package miguel.projetos.oficina.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import miguel.projetos.oficina.Repository.FuncionarioRepository;
import miguel.projetos.oficina.entity.Funcionario;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final FuncionarioRepository repo;
    private final PasswordEncoder encoder;

    public AdminInitializer(FuncionarioRepository repo,
                            PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String cpfAdmin = "12345678910";
        // usa findFuncionarioByCpf e verifica se não existe
        if (repo.findFuncionarioByCpf(cpfAdmin).isEmpty()) {
            Funcionario admin = new Funcionario();
            admin.setId_usuario("User-000");
            admin.setCpf(cpfAdmin);
            admin.setNome("Admin Gerente");
            admin.setSenha(encoder.encode("1234"));
            admin.setCargo("Gerente");
            repo.save(admin);
            System.out.println("Admin Gerente criado com CPF " + cpfAdmin);
        }
    }
}
