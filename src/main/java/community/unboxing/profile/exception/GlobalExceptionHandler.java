package community.unboxing.profile.exception;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import community.unboxing.profile.adapters.web.dto.UbcApiResponse;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { BadRequestException.class })
    public ResponseEntity<UbcApiResponse<?>> handleBadRequest(BadRequestException ex) {
        log.error("Bad Request Error", ex);
        return new ResponseEntity<>(UbcApiResponse.builder()
                .success(false)
                .result(ex.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<UbcApiResponse<?>> handleException(Exception ex) {
        log.error("Internal Server Error", ex);
        return new ResponseEntity<>(UbcApiResponse.builder()
                .success(false)
                .result("Internal Server Error")
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
