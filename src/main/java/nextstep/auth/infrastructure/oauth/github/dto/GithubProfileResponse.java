package nextstep.auth.infrastructure.oauth.github.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GithubProfileResponse {
    private String email;
}
