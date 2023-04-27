package nextstep.github.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GithubProfileResponse {
    private final String email;
}
