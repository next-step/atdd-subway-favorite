package nextstep.unit.login.support;

import nextstep.common.auth.InvalidTokenException;
import nextstep.fixture.AuthFixture;
import nextstep.fixture.MemberFixture;
import nextstep.login.infra.SocialClient;
import nextstep.login.application.dto.response.GithubProfileResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Primary
@Component
public class FakeGithubClient implements SocialClient {

    @Override
    public String getAccessTokenFromGithub(String code) {
        return Arrays.stream(AuthFixture.values())
                .map(auth -> auth.Github_정보())
                .filter(gitHubProfile -> gitHubProfile.권한_증서_코드().equals(code))
                .findFirst()
                .orElseThrow(InvalidTokenException::new)
                .인증_토큰();
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        MemberFixture 회원_정보 = Arrays.stream(AuthFixture.values())
                .filter(auth -> auth.Github_정보().인증_토큰().equals(accessToken))
                .findFirst()
                .orElseThrow(InvalidTokenException::new)
                .회원_정보();

        return new GithubProfileResponse(회원_정보.이메일());
    }
}
