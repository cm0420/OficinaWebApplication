package miguel.projetos.oficina.dto;

import lombok.Data;

@Data
public class CarroDto {
    private String id_carro;
    private String chassi;
    private String placa;
    private String fabricante;
    private String modelo;
    private ClienteDto dono;
}
