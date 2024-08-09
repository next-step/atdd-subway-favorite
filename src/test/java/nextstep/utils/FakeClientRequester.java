package nextstep.utils;

import nextstep.member.application.ClientRequester;
import nextstep.member.application.dto.ProfileResponse;

import static nextstep.utils.GithubResponse.사용자1;

public class FakeClientRequester implements ClientRequester {
    @Override
    public String requestGithubAccessToken(String code) {
        return "requestGithubAccessToken_success";
    }

    @Override
    public ProfileResponse requestGithubProfile(String accessToken) {
        return ProfileResponse.of(사용자1.getEmail(), 사용자1.getAge());
    }
}
