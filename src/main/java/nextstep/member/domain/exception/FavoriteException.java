package nextstep.member.domain.exception;

public class FavoriteException extends RuntimeException {
    public static final String SAME_SOURCE_AND_TARGET = "즐겨찾기의 출발점과 도착역이 같을 수 없습니다.";
    public static final String DUPLICATION_FAVORITE = "이미 등록되어있는 즐겨찾기입니다.";

    public FavoriteException(String message) {
        super(message);
    }
}
