package nextstep.common.exception;

public class TokenException extends BusinessException

{

    public TokenException(final ErrorResponse errorResponse) {
        super(errorResponse);
    }
}
