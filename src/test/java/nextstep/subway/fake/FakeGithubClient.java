package nextstep.subway.fake;

import nextstep.member.application.GithubClient;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Primary
@Component
public class FakeGithubClient extends GithubClient {

    @Override
    public String getAccessTokenFromGithub(String code) {
        return Arrays.stream(GithubResponseFixture.values())
                .filter(githubResponses -> githubResponses.getCode().equals(code))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getAccessToken();
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        return new GithubProfileResponse(
                Arrays.stream(GithubResponseFixture.values())
                .filter(githubResponses -> githubResponses.getAccessToken()
                        .equals(accessToken))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("이메일 정보를 찾을 수 없습니다."))
                        .getEmail());
    }
}
