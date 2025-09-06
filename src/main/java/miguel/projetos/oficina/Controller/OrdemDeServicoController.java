package miguel.projetos.oficina.Controller;

import miguel.projetos.oficina.dto.*;
import miguel.projetos.oficina.entity.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import miguel.projetos.oficina.service.OrdemDeServicoService;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/os")
public class OrdemDeServicoController {
    @Autowired
    private OrdemDeServicoService ordemDeServicoService;

    @GetMapping
    public ResponseEntity<List<OrdemDeServicoDto>> getAll() {
        List<OrdemDeServicoDto> osList = ordemDeServicoService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(osList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemDeServicoDto> getById(@PathVariable String id) {
        OrdemDeServico os = ordemDeServicoService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ordem de Serviço com ID " + id + " não encontrada."));
        return ResponseEntity.ok(convertToDto(os));
    }

    @PostMapping
    public ResponseEntity<OrdemDeServicoDto> create(@Valid @RequestBody OrdemDeServicoCreateDto createDto) {
        // Se o agendamento não existir, o serviço lança IllegalArgumentException (erro 400)
        OrdemDeServico novaOS = ordemDeServicoService.abrirOS(createDto.getAgendamentoId(), createDto.getDefeitoRelatado());
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(novaOS));
    }

    @PutMapping("/{id}/adicionar-peca")
    public ResponseEntity<OrdemDeServicoDto> adicionarPeca(@PathVariable String id, @RequestParam String pecaId, @RequestParam int quantidade) {
        // Exceções de OS não encontrada (404), peça não encontrada (404),
        // ou conflito de status/estoque (409) são tratadas pelo handler.
        OrdemDeServico os = ordemDeServicoService.adicionarPeca(id, pecaId, quantidade);
        return ResponseEntity.ok(convertToDto(os));
    }

    @PutMapping("/{id}/iniciar-inspecao")
    public ResponseEntity<OrdemDeServicoDto> iniciarInspecao(@PathVariable String id) {
        OrdemDeServico os = ordemDeServicoService.iniciarInspecao(id);
        return ResponseEntity.ok(convertToDto(os));
    }

    @PutMapping("/{id}/iniciar-servico")
    public ResponseEntity<OrdemDeServicoDto> iniciarServico(@PathVariable String id) {
        OrdemDeServico os = ordemDeServicoService.iniciarServico(id);
        return ResponseEntity.ok(convertToDto(os));
    }

    @PutMapping("/{id}/finalizar-servico")
    public ResponseEntity<OrdemDeServicoDto> finalizarServico(@PathVariable String id) {
        OrdemDeServico os = ordemDeServicoService.finalizarServico(id);
        return ResponseEntity.ok(convertToDto(os));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<OrdemDeServicoDto> cancelarOS(@PathVariable String id) {
        // Se a OS não puder ser cancelada (ex: já finalizada),
        // o serviço lança IllegalStateException (erro 409).
        OrdemDeServico os = ordemDeServicoService.cancelarOS(id);
        return ResponseEntity.ok(convertToDto(os));
    }

    // --- MÉTODOS AUXILIARES COMPLETOS ---

    private OrdemDeServicoDto convertToDto(OrdemDeServico os) {
        OrdemDeServicoDto dto = new OrdemDeServicoDto();
        dto.setNumero_os(os.getNumero_os());
        dto.setDefeito_relatado(os.getDefeito_relatado());
        dto.setData_abertura(os.getData_abertura());
        dto.setData_fechamento(os.getData_fechamento());
        dto.setStatus(os.getStatus());

        // Conversão completa dos objetos aninhados
        if (os.getCliente() != null) {
            dto.setCliente(convertClienteToDto(os.getCliente()));
        }
        if (os.getCarro() != null) {
            dto.setCarro(convertCarroToDto(os.getCarro()));
        }
        if (os.getMecanico() != null) {
            dto.setMecanico(convertFuncionarioToDto(os.getMecanico()));
        }
        if (os.getPecasUtilizadas() != null) {
            dto.setPecasUtilizadas(os.getPecasUtilizadas().stream()
                    .map(this::convertPecaUtilizadaToDto)
                    .collect(Collectors.toList()));
        }

        // Calcula e define o valor total
        dto.setValorTotal(ordemDeServicoService.calcularValorTotal(os));

        return dto;
    }

    // --- Conversores para DTOs aninhados ---

    private PecaUtilizadaDto convertPecaUtilizadaToDto(PecaUtilizada pecaUtilizada) {
        PecaUtilizadaDto dto = new PecaUtilizadaDto();
        dto.setId(pecaUtilizada.getId());
        dto.setQuantidade_utilizada(pecaUtilizada.getQuantidade_utilizada());
        dto.setPreco_no_momento_do_uso(pecaUtilizada.getPreco_no_momento_do_uso());
        if (pecaUtilizada.getPeca() != null) {
            dto.setNomePeca(pecaUtilizada.getPeca().getNome());
        }
        // Calcula o subtotal para o DTO
        BigDecimal subtotal = pecaUtilizada.getPreco_no_momento_do_uso()
                .multiply(new BigDecimal(pecaUtilizada.getQuantidade_utilizada()));
        dto.setSubtotal(subtotal);
        return dto;
    }

    private ClienteDto convertClienteToDto(Cliente cliente) {
        ClienteDto dto = new ClienteDto();
        dto.setId_cliente(cliente.getId_cliente());
        dto.setCpf(cliente.getCpf());
        dto.setNome(cliente.getNome());
        return dto;
    }

    private CarroDto convertCarroToDto(Carro carro) {
        CarroDto dto = new CarroDto();
        dto.setId_carro(carro.getId_carro());
        dto.setPlaca(carro.getPlaca());
        dto.setModelo(carro.getModelo());
        return dto;
    }

    private FuncionarioDto convertFuncionarioToDto(Funcionario funcionario) {
        FuncionarioDto dto = new FuncionarioDto();
        dto.setId_usuario(funcionario.getId_usuario());
        dto.setNome(funcionario.getNome());
        return dto;
    }
}
