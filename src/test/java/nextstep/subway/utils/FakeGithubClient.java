package nextstep.subway.utils;

import nextstep.member.application.GithubClient;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Profile("test")
@Primary
@Component
public class FakeGithubClient extends GithubClient {

    @Override
    public String getAccessTokenFromGithub(String code) {
        return Arrays.stream(GithubResponses.values())
                .filter(r -> r.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("깃험 코드가 유효하지 않습니다."))
                .getAccessToken();
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        return new GithubProfileResponse(Arrays.stream(GithubResponses.values())
                .filter(r -> r.getAccessToken().equals(accessToken))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("토큰이 유효하지 않습니다."))
                .getEmail());
    }
}
