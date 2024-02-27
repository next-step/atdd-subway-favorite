package nextstep.favorite.domain.exception;

public class FavoriteMemberException extends FavoriteException {
    public static String DEFAULT_MESSAGE = "수행할 수 없는 멤버입니다.";
    public FavoriteMemberException() {
        super(DEFAULT_MESSAGE);
    }

    public FavoriteMemberException(String message) {
        super(message);
    }
}
