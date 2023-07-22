package subway.auth.token.oauth2.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class GithubAccessTokenRequest {

    private String code;
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;

    public String getCode() {
        return code;
    }
}
