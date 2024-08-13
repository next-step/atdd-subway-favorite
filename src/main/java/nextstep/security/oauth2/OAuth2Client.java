package nextstep.security.oauth2;

public interface OAuth2Client {

    AccessTokenResponse getAccessToken(String code);

    Object getUserInfo(String accessToken);

}
