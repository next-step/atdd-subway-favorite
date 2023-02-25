package nextstep.member.exception;

public class FavoriteRestApiException extends RuntimeException{
    public FavoriteRestApiException() {
    }

    public FavoriteRestApiException(String message) {
        super(message);
    }
}
