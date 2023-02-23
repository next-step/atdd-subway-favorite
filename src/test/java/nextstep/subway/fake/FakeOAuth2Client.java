package nextstep.subway.fake;

import nextstep.member.auth.OAuth2Client;
import nextstep.member.auth.OAuth2User;
import org.springframework.context.annotation.Profile;

@Profile("test")
public class FakeOAuth2Client implements OAuth2Client {

    @Override
    public String getAccessToken(String code) {
        return GithubResponses.findAccessCodeByCode(code);
    }

    @Override
    public OAuth2User loadUser(String accessToken) {
        return GithubResponses.findUserByAccessToken(accessToken);
    }
}
