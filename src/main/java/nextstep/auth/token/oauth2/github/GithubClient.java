package nextstep.auth.token.oauth2.github;

import nextstep.auth.AuthenticationException;
import nextstep.auth.ForbiddenException;

public interface GithubClient {
    String getAccessTokenFromGithub(String code) throws AuthenticationException;
    GithubProfileResponse getGithubProfileFromGithub(String accessToken) throws ForbiddenException;
}
