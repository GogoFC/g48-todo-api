package se.lexicon.g48todoapi.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        StringBuilder errorDetails = new StringBuilder();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errorDetails.append(fieldError.getField());
            errorDetails.append(": ");
            errorDetails.append(fieldError.getDefaultMessage());
            errorDetails.append(", ");
        });

        DTOError dtoError = DTOError.builder()
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .statusText(errorDetails.toString())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dtoError);

    }

    @ExceptionHandler({DataNotFoundException.class, DataDuplicateException.class})
    public ResponseEntity<DTOError> handleDataNotFoundException(DataNotFoundException ex){

        DTOError dtoError = DTOError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .statusText(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().body(dtoError);
    }

}
