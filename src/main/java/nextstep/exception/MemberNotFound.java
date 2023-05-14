package nextstep.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MemberNotFound extends AuthException{

    private static final String MESSAGE = "찾는 회원이 존재하지 않습니다.";

    public MemberNotFound() {
        super(MESSAGE);
    }

    public MemberNotFound(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}
