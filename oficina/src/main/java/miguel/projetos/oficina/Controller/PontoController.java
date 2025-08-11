package miguel.projetos.oficina.Controller;

import jakarta.validation.Valid;
import miguel.projetos.oficina.dto.PontoRequestDto;
import miguel.projetos.oficina.dto.SuccessResponseDto;
import miguel.projetos.oficina.entity.Funcionario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import miguel.projetos.oficina.service.PontoService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/ponto")
public class PontoController {

    @Autowired
    private PontoService pontoService;

    @PostMapping("/entrada")
    public ResponseEntity<SuccessResponseDto> baterPontoEntrada(@Valid @RequestBody PontoRequestDto dto) {
        // Se o funcionário não existir (404) ou já tiver ponto aberto (409),
        // as exceções serão tratadas pelo RestExceptionHandler.
        pontoService.baterPontoEntrada(dto.getCpf());

        SuccessResponseDto response = new SuccessResponseDto(
                LocalDateTime.now(),
                "Ponto de entrada registrado com sucesso."
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/saida")
    public ResponseEntity<SuccessResponseDto> baterPontoSaida(@Valid @RequestBody PontoRequestDto dto) {
        // Se o funcionário não existir (404) ou não tiver ponto aberto (409),
        // as exceções serão tratadas pelo RestExceptionHandler.
        pontoService.baterPontoSaida(dto.getCpf());

        SuccessResponseDto response = new SuccessResponseDto(
                LocalDateTime.now(),
                "Ponto de saída registrado com sucesso."
        );
        return ResponseEntity.ok(response);
    }
}
