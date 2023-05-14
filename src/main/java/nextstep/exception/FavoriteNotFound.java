package nextstep.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FavoriteNotFound extends AuthException{

    private static final String MESSAGE = "즐겨찾기가 존재하지 않습니다.";

    public FavoriteNotFound() {
        super(MESSAGE);
    }

    public FavoriteNotFound(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}
