package nextstep.subway.auth.client.dto;

import nextstep.subway.auth.client.github.dto.GithubProfileResponse;

public class ProfileResponseFactory {
    public static ProfileResponse create(GithubProfileResponse response) {
        return new ProfileResponse(response.getEmail(), response.getAge());
    }
}
