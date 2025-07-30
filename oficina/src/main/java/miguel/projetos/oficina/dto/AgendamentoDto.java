package miguel.projetos.oficina.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgendamentoDto {
    private Long id;
    private LocalDateTime data_hora;
    private String tipo_servico;
    private ClienteDto cliente;
    private CarroDto carro;
    private FuncionarioDto mecanico;
}
