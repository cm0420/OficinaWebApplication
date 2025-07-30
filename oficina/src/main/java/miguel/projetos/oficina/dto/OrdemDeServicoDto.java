package miguel.projetos.oficina.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrdemDeServicoDto {
    private String numero_os;
    private String defeito_relatado;
    private LocalDateTime data_abertura;
    private LocalDateTime data_fechamento;
    private String status;
    private ClienteDto cliente;
    private CarroDto carro;
    private FuncionarioDto mecanico;
    private List<PecaUtilizadaDto> pecasUtilizadas;
    private BigDecimal valorTotal;
}
