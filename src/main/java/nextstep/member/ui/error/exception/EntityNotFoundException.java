package nextstep.member.ui.error.exception;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
