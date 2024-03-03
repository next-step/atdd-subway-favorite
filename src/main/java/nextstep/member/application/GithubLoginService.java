package nextstep.member.application;

import nextstep.member.application.dto.OAuth2Request;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GithubLoginService {

    private final OAuth2Client githubClient;

    // bean name
    public GithubLoginService(@Qualifier("githubClient") OAuth2Client githubClient) {
        this.githubClient = githubClient;
    }


    public TokenResponse login(OAuth2Request oAuth2Request) {
        return new TokenResponse();
    }
}
