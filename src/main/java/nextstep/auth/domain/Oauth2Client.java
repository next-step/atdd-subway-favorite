package nextstep.auth.domain;

public interface Oauth2Client {

    String getAccessToken(String code);

    ProfileResponse getProfile(String accessToken);
}
