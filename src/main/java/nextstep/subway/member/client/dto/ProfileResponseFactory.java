package nextstep.subway.member.client.dto;

import nextstep.subway.member.client.github.dto.GithubProfileResponse;

public class ProfileResponseFactory {
    public static ProfileResponse create(GithubProfileResponse response) {
        return new ProfileResponse(response.getEmail(), response.getAge());
    }
}
