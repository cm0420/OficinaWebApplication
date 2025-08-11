package miguel.projetos.oficina.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SuccessResponseDto {

    private LocalDateTime timestamp;
    private String message;
}
