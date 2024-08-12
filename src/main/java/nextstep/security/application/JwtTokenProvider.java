package nextstep.security.application;

public interface JwtTokenProvider<INFO> {
    String createToken(INFO tokenInfo);

    INFO parseToken(String token);

}
