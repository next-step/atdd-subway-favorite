package nextstep.member.common.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
    }
}
