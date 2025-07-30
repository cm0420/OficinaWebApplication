package miguel.projetos.oficina.Controller;

import miguel.projetos.oficina.entity.Funcionario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import miguel.projetos.oficina.service.PontoService;

@RestController
@RequestMapping("/api/ponto")
public class PontoController {

    @Autowired
    private PontoService pontoService;

    @PostMapping("/entrada")
    public ResponseEntity<String> baterPontoEntrada(@RequestBody Funcionario funcionario) {
        try {
            pontoService.baterPontoEntrada(funcionario);
            return ResponseEntity.ok("Ponto de entrada registrado com sucesso.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/saida")
    public ResponseEntity<String> baterPontoSaida(@RequestBody Funcionario funcionario) {
        try {
            pontoService.baterPontoSaida(funcionario);
            return ResponseEntity.ok("Ponto de saída registrado com sucesso.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
