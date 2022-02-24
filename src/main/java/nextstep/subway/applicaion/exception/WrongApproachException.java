package nextstep.subway.applicaion.exception;

import org.springframework.http.HttpStatus;

public class WrongApproachException extends BusinessException{

    public static final String MESSAGE = "당신의 즐겨찾기 아님";
    private static HttpStatus code = HttpStatus.FORBIDDEN;

    public WrongApproachException() {
        super(MESSAGE,code);
    }
}
