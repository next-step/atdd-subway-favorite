package nextstep.security.oauth2;

public interface GithubOauth2Client extends OAuth2Client {
    GithubUserInfoResponse getUserInfo(String accessToken);

}
