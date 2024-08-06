package nextstep.common;

import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends SubwayException {
    public MemberNotFoundException(String email) {
        super("사용자를 찾을 수 없습니다. email: " + email, HttpStatus.BAD_REQUEST);
    }
}
