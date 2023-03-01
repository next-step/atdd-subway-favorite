package nextstep.subway.domain.exception;

public class NotFoundPathException extends RuntimeException {

    public NotFoundPathException() {
        super("경로를 찾지 못하였습니다");
    }

}

