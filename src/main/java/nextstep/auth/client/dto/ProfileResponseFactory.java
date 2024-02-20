package nextstep.auth.client.dto;

import nextstep.auth.client.github.dto.GithubProfileResponse;

public class ProfileResponseFactory {
    public static ProfileResponse create(GithubProfileResponse response) {
        return new ProfileResponse(response.getEmail(), response.getAge());
    }
}
