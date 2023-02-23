package nextstep.member.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GithubAccessTokenResponse {
    private String accessToken;

    public static GithubAccessTokenResponse of(String accessToken) {
        return new GithubAccessTokenResponse(accessToken);
    }

}
