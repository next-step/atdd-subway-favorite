package nextstep.utils.fakeMock;

import nextstep.auth.application.ClientRequester;
import nextstep.auth.application.dto.ProfileResponse;

import static nextstep.utils.dtoMock.GithubResponse.사용자1;

public class FakeClientRequester implements ClientRequester {
    @Override
    public String requestAccessToken(String code) {
        return "requestGithubAccessToken_success";
    }

    @Override
    public ProfileResponse requestProfile(String accessToken) {
        return ProfileResponse.of(사용자1.getEmail(), 사용자1.getAge());
    }
}

