package nextstep.member.infra;

import java.util.List;
import nextstep.member.auth.BaseOAuth2User;
import nextstep.member.auth.OAuth2Client;
import nextstep.member.auth.OAuth2User;
import nextstep.member.auth.interceptor.TokenAuthenticationInterceptor;
import nextstep.member.infra.dto.GithubAccessTokenRequest;
import nextstep.member.infra.dto.GithubAccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubClient implements OAuth2Client {

    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;
    private final String tokenUrl;
    private final String profileUrl;

    public GithubClient(
        RestTemplate restTemplate,
        @Value("${github.client.id}") String clientId,
        @Value("${github.client.secret}") String clientSecret,
        @Value("${github.url.access-token}") String tokenUrl,
        @Value("${github.url.profile}") String profileUrl
    ) {
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenUrl = tokenUrl;
        this.profileUrl = profileUrl;
    }

    @Override
    public String getAccessToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
            code,
            clientId,
            clientSecret
        );

        final GithubAccessTokenResponse response = restTemplate.postForObject(
            tokenUrl,
            githubAccessTokenRequest,
            GithubAccessTokenResponse.class
        );

        String accessToken = response.getAccessToken();
        if (accessToken == null) {
            throw new RuntimeException();
        }
        return accessToken;
    }

    @Override
    public OAuth2User loadUser(String accessToken) {
        try {
            restTemplate.setInterceptors(List.of(new TokenAuthenticationInterceptor(accessToken)));

            final ResponseEntity<BaseOAuth2User> response = restTemplate.getForEntity(
                profileUrl,
                BaseOAuth2User.class
            );

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException();
        }
    }
}
