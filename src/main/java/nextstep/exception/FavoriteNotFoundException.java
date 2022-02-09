package nextstep.exception;

public class FavoriteNotFoundException extends ServiceException {

    private static final String MESSAGE = "즐겨찾기를 찾을 수 없습니다 - %d";

    public FavoriteNotFoundException(long id) {
        super(String.format(MESSAGE, id));
    }

}
