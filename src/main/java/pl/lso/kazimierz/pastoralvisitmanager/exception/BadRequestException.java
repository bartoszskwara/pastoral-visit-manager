package pl.lso.kazimierz.pastoralvisitmanager.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
