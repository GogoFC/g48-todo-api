package se.lexicon.g48todoapi.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record ApiError(@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime timestamp,
                       Integer status, String error, String path, Map<String,String> violations) {
}
