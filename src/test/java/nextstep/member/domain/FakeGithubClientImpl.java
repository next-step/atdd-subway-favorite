package nextstep.member.domain;

import nextstep.auth.domain.GithubProfileResponse;
import nextstep.auth.domain.Oauth2Client;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import static nextstep.common.constants.ErrorConstant.INVALID_AUTHENTICATION_INFO;

@Component
@Primary
public class FakeGithubClientImpl implements Oauth2Client {

    private GithubResponses response;

    @Override
    public String getAccessToken(final String code) {
        try {
            response = GithubResponses.ofCode(code);
        } catch (RuntimeException e) {
            throw new RuntimeException(INVALID_AUTHENTICATION_INFO);
        }

        return response.getAccessToken();
    }

    @Override
    public GithubProfileResponse getProfile(final String accessToken) {
        try {
            response = GithubResponses.ofAccessToken(accessToken);
        } catch (RuntimeException e) {
            throw new RuntimeException(INVALID_AUTHENTICATION_INFO);
        }

        return new GithubProfileResponse(response.getEmail());
    }
}
