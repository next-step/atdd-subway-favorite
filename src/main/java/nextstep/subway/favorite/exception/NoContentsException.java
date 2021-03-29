package nextstep.subway.favorite.exception;

public class NoContentsException extends RuntimeException{
    public NoContentsException() {
        super("삭제할 즐겨찾기가 없습니다.");
    }
}
