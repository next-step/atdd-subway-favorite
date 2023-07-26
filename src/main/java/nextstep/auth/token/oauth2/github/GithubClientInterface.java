package nextstep.auth.token.oauth2.github;

public interface GithubClientInterface {


    String getAccessTokenFromGithub(String code);

    GithubProfileResponse getGithubProfileFromGithub(String accessToken);
}
