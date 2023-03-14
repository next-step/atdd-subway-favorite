package nextstep.subway.fake;

import static nextstep.exception.ExceptionMsg.GITHUB_CODE_IS_NOT_VALID;
import static nextstep.exception.ExceptionMsg.GITHUB_EXCEPTION;

import java.util.Arrays;
import nextstep.exception.ApiException;
import nextstep.member.application.GithubClient;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Profile("test")
@Primary
@Component
public class FakeGithubClient extends GithubClient {

    @Override
    public String getAccessTokenFromGithub(String code) {
        return Arrays.stream(GithubResponses.values())
            .filter(r -> r.getCode().equals(code))
            .findFirst()
            .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, GITHUB_CODE_IS_NOT_VALID))
            .getAccessToken();
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        return new GithubProfileResponse(Arrays.stream(GithubResponses.values())
            .filter(r -> r.getAccessToken().equals(accessToken))
            .findFirst()
            .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, GITHUB_EXCEPTION))
            .getEmail());
    }
}
