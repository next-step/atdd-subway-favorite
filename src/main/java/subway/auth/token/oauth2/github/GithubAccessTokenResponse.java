package subway.auth.token.oauth2.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class GithubAccessTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    private String scope;
    private String bearer;

    public String getAccessToken() {
        return accessToken;
    }
}
