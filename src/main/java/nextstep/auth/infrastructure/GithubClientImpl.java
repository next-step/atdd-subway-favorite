package nextstep.auth.infrastructure;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.GithubClient;
import nextstep.auth.application.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class GithubClientImpl implements GithubClient {
    private final RestTemplate restTemplate;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.token.url}")
    private String tokenUrl;

    @Value("${github.api.url}")
    private String apiUrl;

    @Override
    public GithubAccessTokenResponse getAccessTokenFromGithub(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
                code,
                clientId,
                clientSecret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<GithubAccessTokenRequest> httpEntity = new HttpEntity<>(githubAccessTokenRequest, headers);

        ResponseEntity<GithubAccessTokenResponse> response = restTemplate
                .exchange(tokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class);

        return response.getBody();
    }

    @Override
    public GithubProfileResponse requestGithubProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<GithubProfileResponse> response = restTemplate
                .exchange(apiUrl + "/user", HttpMethod.GET, entity, GithubProfileResponse.class);

        return response.getBody();
    }
}
