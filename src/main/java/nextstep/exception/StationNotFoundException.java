package nextstep.exception;

public class StationNotFoundException extends ServiceException {

    private static final String MESSAGE = "역을 찾을 수 없습니다 - %d";

    public StationNotFoundException(long id) {
        super(String.format(MESSAGE, id));
    }

}
