package nextstep.infra.github.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class GithubAccessTokenRequest {
    private String code;

    private String clientId;

    private String clientSecret;
}
