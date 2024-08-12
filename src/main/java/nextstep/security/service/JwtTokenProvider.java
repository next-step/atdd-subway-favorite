package nextstep.security.service;

public interface JwtTokenProvider<INFO> {
    String createToken(INFO tokenInfo);

    INFO parseToken(String token);

}
