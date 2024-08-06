package nextstep.member.domain;

import nextstep.member.payload.AccessTokenRequest;
import nextstep.member.payload.AccessTokenResponse;
import nextstep.member.payload.GithubUserInfoResponse;
import org.springframework.stereotype.Component;

@Component
public class GithubOAuth2ClientImpl implements GithubOAuth2Client {

    @Override
    public AccessTokenResponse getAccessToken(final AccessTokenRequest request) {
        return null;
    }

    @Override
    public GithubUserInfoResponse getUserInfo(final String accessToken) {
        return null;
    }
}
