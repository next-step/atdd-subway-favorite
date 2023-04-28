package nextstep.subway.utils;

import nextstep.exception.InvalidCodeException;
import nextstep.exception.InvalidTokenException;
import nextstep.github.GithubClient;
import nextstep.github.application.dto.GithubProfileResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Primary
@Component
public class FakeGithubClient extends GithubClient {

    @Override
    public String getAccessTokenFromGithub(String code) {
        return Arrays.stream(GithubResponses.values())
                .filter(it -> it.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new InvalidCodeException())
                .getAccessToken();
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        return new GithubProfileResponse(Arrays.stream(GithubResponses.values())
                .filter(it -> it.getAccessToken().equals(accessToken))
                .findFirst()
                .orElseThrow(() -> new InvalidTokenException())
                .getEmail());
    }
}
