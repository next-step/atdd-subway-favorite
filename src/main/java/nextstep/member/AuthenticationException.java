package nextstep.member;

//@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "UNAUTHORIZED")
public class AuthenticationException extends RuntimeException {
    private String message;

    public AuthenticationException() {

    }

    public AuthenticationException(String message) {
        this.message = message;
    }
}
