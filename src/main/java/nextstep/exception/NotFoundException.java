package nextstep.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("데이터가 존재하지 않습니다.");
    }
}
