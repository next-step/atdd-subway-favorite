package nextstep.subway.unit;

import nextstep.member.application.GithubClient;

public class StubGithubClient implements GithubClient {

    @Override
    public String getAccessTokenFromGithub(String code) {
        return GithubSampleResponse.findByCode(code).getAccessToken();
    }
}
