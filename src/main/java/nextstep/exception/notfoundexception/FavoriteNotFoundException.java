package nextstep.exception.notfoundexception;

public class FavoriteNotFoundException extends NotFoundException {
    public static final String ERROR_MESSAGE = "Favorite 이 존재하지 않습니다.";

    public FavoriteNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
