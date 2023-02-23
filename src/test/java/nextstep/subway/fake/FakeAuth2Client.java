package nextstep.subway.fake;

import nextstep.member.auth.Auth2Client;
import nextstep.member.auth.OAuth2User;

public class FakeAuth2Client implements Auth2Client {

    @Override
    public String getAccessToken(String code) {
        return GithubResponses.findAccessCodeByCode(code);
    }

    @Override
    public OAuth2User loadUser(String accessToken) {
        return GithubResponses.findUserByAccessToken(accessToken);
    }
}
