package nextstep.member.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class GithubClient {

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.url.access-token}")
    private String tokenUrl;
    @Value("${github.url.profile}")
    private String profileUrl;

    private final RestTemplate restTemplate;

    public GithubClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getAccessTokenFromGithub(String code) {
        final var request = new GithubAccessTokenRequest(
            code,
            clientId,
            clientSecret
        );
        final var response = restTemplate.postForObject(tokenUrl, request, GithubAccessTokenResponse.class);

        String accessToken = response.getAccessToken();
        if (accessToken == null) {
            throw new RuntimeException();
        }
        return accessToken;
    }

    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        final var interceptor = new TokenAuthInterceptor(accessToken);
        restTemplate.setInterceptors(List.of(interceptor));

        try {
            return restTemplate.getForObject(profileUrl, GithubProfileResponse.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException();
        }
    }
}
