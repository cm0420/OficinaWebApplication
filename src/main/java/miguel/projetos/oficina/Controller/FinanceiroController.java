package miguel.projetos.oficina.Controller;

import miguel.projetos.oficina.dto.RegistroFinanceiroDto;
import miguel.projetos.oficina.dto.RelatorioMensalDto;
import miguel.projetos.oficina.dto.HoleriteDto;
import miguel.projetos.oficina.entity.RegistroFinanceiro;
import miguel.projetos.oficina.service.FinanceiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/financeiro")
public class FinanceiroController {

    @Autowired
    private FinanceiroService financeiroService;

    // 🔹 Listar registros financeiros
    @GetMapping("/registros")
    public ResponseEntity<List<RegistroFinanceiroDto>> getRegistros() {
        List<RegistroFinanceiroDto> registrosDto = financeiroService.findAllRegistros().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(registrosDto);
    }

    // 🔹 Balanço geral
    @GetMapping("/balanco")
    public ResponseEntity<Map<String, BigDecimal>> getBalanco() {
        return ResponseEntity.ok(financeiroService.getBalancoFinanceiro());
    }

    // 🔹 Pagar salários
    @PostMapping("/pagar-salarios")
    public ResponseEntity<String> pagarSalarios() {
        financeiroService.pagarSalarios();
        return ResponseEntity.ok("Salários pagos com sucesso e despesa registrada.");
    }

    // 🔹 Relatório mensal consolidado
    @GetMapping("/relatorio/mensal/{ano}/{mes}")
    public ResponseEntity<RelatorioMensalDto> getRelatorioMensal(
            @PathVariable int ano,
            @PathVariable int mes) {
        return ResponseEntity.ok(financeiroService.getRelatorioMensal(ano, mes));
    }

    // 🔹 Holerite individual
    @GetMapping("/holerite/{id}/{ano}/{mes}")
    public ResponseEntity<HoleriteDto> getHolerite(
            @PathVariable String id,
            @PathVariable int ano,
            @PathVariable int mes) {
        return ResponseEntity.ok(financeiroService.getHolerite(id, ano, mes));
    }

    // ========= PRIVATE HELPER =========
    private RegistroFinanceiroDto convertToDto(RegistroFinanceiro registro) {
        RegistroFinanceiroDto dto = new RegistroFinanceiroDto();
        dto.setId(registro.getId());
        dto.setDescricao(registro.getDescricao());
        dto.setValor(registro.getValor());
        dto.setTipo(registro.getTipo());
        dto.setData(registro.getData());
        return dto;
    }
}
