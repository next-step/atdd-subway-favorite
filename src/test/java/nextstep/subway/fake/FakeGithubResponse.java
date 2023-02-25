package nextstep.subway.fake;

import nextstep.member.application.dto.GithubResponses;

import java.util.Arrays;

public class FakeGithubResponse {

    public static String getAccessToken(String code) {
        return Arrays.stream(GithubResponses.values()).filter(githubResponses -> githubResponses.getCode().equals(code))
            .findFirst()
            .orElseThrow(RuntimeException::new)
            .getAccessToken();
    }

}
