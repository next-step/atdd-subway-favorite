package nextstep.member.domain.exception;

public class CantAddFavoriteException extends RuntimeException {
    public static final String INVALID_SOURCE_AND_TARGET = "즐겨찾기의 출발점과 종점이 같을 수 없습니다.";
    public static final String ALREADY_ADDED = "이미 존재하는 즐겨찾기입니다.";

    public CantAddFavoriteException(String message) {
        super(message);
    }
}
