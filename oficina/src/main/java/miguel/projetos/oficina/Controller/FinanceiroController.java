package miguel.projetos.oficina.Controller;

import miguel.projetos.oficina.dto.RegistroFinanceiroDto;
import miguel.projetos.oficina.entity.RegistroFinanceiro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import miguel.projetos.oficina.service.FinanceiroService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/financeiro")
public class FinanceiroController {
    @Autowired
    private FinanceiroService financeiroService;


    @GetMapping("/registros")
    public ResponseEntity<List<RegistroFinanceiroDto>> getRegistros() {
        List<RegistroFinanceiroDto> registrosDto = financeiroService.findAllRegistros().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(registrosDto);
    }


    @GetMapping("/balanco")
    public ResponseEntity<Map<String, BigDecimal>> getBalanco() {
        return ResponseEntity.ok(financeiroService.getBalancoFinanceiro());
    }


    @PostMapping("/pagar-salarios")
    public ResponseEntity<String> pagarSalarios() {
        financeiroService.pagarSalarios();
        return ResponseEntity.ok("Salários pagos com sucesso e despesa registrada.");
    }


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
