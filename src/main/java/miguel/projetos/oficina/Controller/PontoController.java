package miguel.projetos.oficina.Controller;

import jakarta.validation.Valid;
import miguel.projetos.oficina.dto.PontoRequestDto;
import miguel.projetos.oficina.dto.PontoDto;
import miguel.projetos.oficina.dto.ResumoPontoDto;
import miguel.projetos.oficina.dto.SuccessResponseDto;
import miguel.projetos.oficina.service.PontoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ponto")
public class PontoController {

    @Autowired
    private PontoService pontoService;

    @PostMapping("/entrada")
    public ResponseEntity<SuccessResponseDto> baterPontoEntrada(@Valid @RequestBody PontoRequestDto dto) {
        pontoService.baterPontoEntrada(dto.getCpf());
        SuccessResponseDto response = new SuccessResponseDto(
                LocalDateTime.now(),
                "Ponto de entrada registrado com sucesso."
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/saida")
    public ResponseEntity<SuccessResponseDto> baterPontoSaida(@Valid @RequestBody PontoRequestDto dto) {
        pontoService.baterPontoSaida(dto.getCpf());
        SuccessResponseDto response = new SuccessResponseDto(
                LocalDateTime.now(),
                "Ponto de saída registrado com sucesso."
        );
        return ResponseEntity.ok(response);
    }

    // 🔹 Listar pontos de um funcionário no mês
    @GetMapping("/funcionario/{cpf}/{ano}/{mes}")
    public ResponseEntity<List<PontoDto>> getPontosDoFuncionario(
            @PathVariable String cpf,
            @PathVariable int ano,
            @PathVariable int mes) {
        return ResponseEntity.ok(pontoService.getPontosDoFuncionario(cpf, ano, mes));
    }

    // 🔹 Resumo de um funcionário no mês
    @GetMapping("/resumo/{cpf}/{ano}/{mes}")
    public ResponseEntity<ResumoPontoDto> getResumoMensal(
            @PathVariable String cpf,
            @PathVariable int ano,
            @PathVariable int mes) {
        return ResponseEntity.ok(pontoService.getResumoMensal(cpf, ano, mes));
    }

    // 🔹 Relatório geral de todos os funcionários no mês
    @GetMapping("/relatorio/{ano}/{mes}")
    public ResponseEntity<List<ResumoPontoDto>> getRelatorioMensal(
            @PathVariable int ano,
            @PathVariable int mes) {
        return ResponseEntity.ok(pontoService.getRelatorioMensal(ano, mes));
    }
}
