package pl.lso.kazimierz.pastoralvisitmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity handleNotFoundException(NotFoundException e) {

        Map<String, String> response = new HashMap<>();
        response.put("response", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ServerException.class})
    protected ResponseEntity<?> handleServerException(ServerException e) {

        Map<String, String> response = new HashMap<>();
        response.put("response", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    protected ResponseEntity<?> handleBadRequestException(BadRequestException e) {

        Map<String, String> response = new HashMap<>();
        response.put("response", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ForbiddenException.class})
    protected ResponseEntity<?> handleForbiddenException(ForbiddenException e) {

        Map<String, String> response = new HashMap<>();
        response.put("response", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {

        List<Map> result = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            Map<String, String> error = new HashMap<>();
            error.put("error",  err.getDefaultMessage());
            result.add(error);
        });
        Map<String, Object> response = new HashMap<>();
        response.put("errors", result);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
