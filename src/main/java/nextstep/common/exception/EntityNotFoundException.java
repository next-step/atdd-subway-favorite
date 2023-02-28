package nextstep.common.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(String.format("%s is not found.", message));
    }

    public EntityNotFoundException(Long id, String message) {
        super(String.format("%s %s is not found.", id, message));
    }
}
