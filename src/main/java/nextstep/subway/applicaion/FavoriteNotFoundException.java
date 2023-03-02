package nextstep.subway.applicaion;

public class FavoriteNotFoundException extends RuntimeException {

    private final static String MESSAGE = "회원의 즐겨찾기 목록을 찾을 수 없습니다. (요청값: %f)";

    public FavoriteNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
