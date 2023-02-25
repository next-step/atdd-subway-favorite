package nextstep.member.common;

public class BusinessException extends RuntimeException {
    public BusinessException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
    }
}
