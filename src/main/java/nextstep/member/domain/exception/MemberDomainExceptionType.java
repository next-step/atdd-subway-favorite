package nextstep.member.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberDomainExceptionType {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR", "internal server error"),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND.value(), "NOT_FOUND_MEMBER", "not found member"),
    INVALID_SOCIAL_LOGIN_PROVIDER(HttpStatus.BAD_REQUEST.value(), "INVALID_SOCIAL_LOGIN_PROVIDER", "invalid social login provider"),
    ;

    private final int status;
    private final String name;
    private final String message;

    MemberDomainExceptionType(int status, String name, String message) {
        this.status = status;
        this.name = name;
        this.message = message;
    }
}
