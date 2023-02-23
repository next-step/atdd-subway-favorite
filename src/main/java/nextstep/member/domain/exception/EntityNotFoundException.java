package nextstep.member.domain.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(String.format("%s is not found.", message));
    }
}
