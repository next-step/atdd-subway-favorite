package nextstep.subway.config;

import nextstep.member.application.dto.GithubResponse;
import nextstep.member.ui.GithubOauthAdapter;

public class MockGithubOauthAdapter implements GithubOauthAdapter {

    @Override
    public GithubResponse login(String code) {
        return MockGithubResponse.parse(code);
    }
}
