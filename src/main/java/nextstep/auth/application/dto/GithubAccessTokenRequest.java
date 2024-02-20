package nextstep.auth.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GithubAccessTokenRequest {
    private String code;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

    public static GithubAccessTokenRequest of(String code, String clientId, String clientSecret) {
        return new GithubAccessTokenRequest(code, clientId, clientSecret);
    }

}
