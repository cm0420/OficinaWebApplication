package miguel.projetos.oficina.Config;

import miguel.projetos.oficina.dto.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Constrói a resposta de erro padronizada.
     * @param status O HttpStatus da resposta.
     * @param message A mensagem de erro detalhada.
     * @param request O WebRequest original para extrair o path.
     * @return Um ResponseEntity contendo o ErrorResponseDto.
     */
    private ResponseEntity<ErrorResponseDto> buildErrorResponse(HttpStatus status, String message, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );
        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Trata erros de validação de DTOs anotados com @Valid.
     * Retorna um erro 400 (Bad Request) com uma lista dos campos inválidos.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = "Erro de validação: " + ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("'%s': %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                status.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                path
        );

        return new ResponseEntity<>(errorResponse, headers, status);
    }

    /**
     * Trata erros de recursos não encontrados.
     * Lançado por .orElseThrow() em Optionals vazios.
     * Retorna 404 (Not Found).
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseDto> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "O recurso solicitado não foi encontrado.", request);
    }

    /**
     * Trata erros de argumentos inválidos passados para os métodos do serviço.
     * Retorna 400 (Bad Request).
     * Ex: Tentar criar um recurso que já existe (CPF duplicado).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Trata conflitos de regra de negócio.
     * Retorna 409 (Conflict).
     * Ex: Tentar apagar um cliente que ainda possui carros.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /**
     * Trata erros de permissão do Spring Security.
     * Retorna 403 (Forbidden).
     * Ex: Um utilizador 'MECANICO' tenta aceder a um endpoint de 'GERENTE'.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Acesso negado. Você não tem permissão para aceder a este recurso.", request);
    }

    /**
     * Handler genérico para qualquer outra exceção não tratada.
     * Garante que nenhum erro inesperado vaze para o cliente.
     * Retorna 500 (Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex, WebRequest request) {
        // Para depuração, é útil logar a exceção completa no servidor
        // log.error("Ocorreu um erro inesperado", ex);
        String message = "Ocorreu um erro inesperado no servidor. Por favor, tente novamente mais tarde.";
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, request);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "CPF ou senha inválidos.", request);
    }
}
