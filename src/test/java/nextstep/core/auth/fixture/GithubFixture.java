package nextstep.core.auth.fixture;

import nextstep.core.auth.application.dto.GithubCodeRequest;

public class GithubFixture {
    public static GithubCodeRequest 코드없는_로그인_정보() {
        return new GithubCodeRequest();
    }
}
