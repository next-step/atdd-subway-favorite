package nextstep.auth.oauth2.github.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GithubAccessTokenRequest {

    private String code;

    private String client_id;

    private String client_secret;

}
