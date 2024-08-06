package nextstep.auth.domain.infrastructure.github.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GithubProfileResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final String location;
    private final String avatarUrl;
}