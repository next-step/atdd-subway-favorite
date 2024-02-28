package nextstep.member.application.oauth;

import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.ui.dto.GithubLoginRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubClient {
    @Value("${oauth2.github.base-url}")
    private String baseUrl;
    @Value("${oauth2.github.client-id}")
    private String clientId;
    @Value("${oauth2.github.client-secret}")
    private String clientSecret;

    public GithubClient() {}

    public String requestToken(GithubLoginRequest request) {
        String requestTokenPath = "/github/login/oauth/access_token";

        GithubTokenRequest tokenRequest = new GithubTokenRequest(request.getCode(), clientId, clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(tokenRequest, headers);

        String accessToken = new RestTemplate()
                .exchange(baseUrl + requestTokenPath, HttpMethod.POST, httpEntity, TokenResponse.class)
                .getBody()
                .getAccessToken();

        return accessToken;
    }
}
