package nextstep.auth.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 일단 주석한 이유는 응답 바디에 메시지를 전달하고 싶어서 리뷰어님과 상의 후 처리 하려고합니다.
// 리뷰 코멘트로 상세 내용은 달았습니다!!
//@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }
}
