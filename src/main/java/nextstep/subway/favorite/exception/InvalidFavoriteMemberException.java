package nextstep.subway.favorite.exception;

public class InvalidFavoriteMemberException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE_INVALID_FAVORITE_MEMBER = "사용자가 추가한 즐겨찾기만 접근할 수 있습니다. 다시 확인해주세요.";

    public InvalidFavoriteMemberException() {
        super(EXCEPTION_MESSAGE_INVALID_FAVORITE_MEMBER);
    }
}
