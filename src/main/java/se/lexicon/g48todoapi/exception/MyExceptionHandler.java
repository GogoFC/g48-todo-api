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
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler {




    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errorDetails = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(fieldError -> errorDetails.put(fieldError.getField(), fieldError.getDefaultMessage())
        );

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(HttpStatus.valueOf(status.value()).toString())
                .violations(errorDetails)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);

    }

    @ExceptionHandler({DataNotFoundException.class, DataDuplicateException.class})
    public ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException ex, HttpStatusCode status){

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(HttpStatus.valueOf(status.value()).toString())
                .build();

        return ResponseEntity.badRequest().body(apiError);
    }

}
