package nextstep.api.auth.application.token.oauth2.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import nextstep.api.auth.application.token.oauth2.github.dto.GithubAccessTokenRequest;
import nextstep.api.auth.application.token.oauth2.github.dto.GithubAccessTokenResponse;
import nextstep.api.auth.application.token.oauth2.github.dto.GithubProfileResponse;

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

    public String getAccessTokenFromGithub(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
            code,
            clientId,
            clientSecret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
            githubAccessTokenRequest, headers);
        RestTemplate restTemplate = new RestTemplate();

        String accessToken = restTemplate
            .exchange(tokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
            .getBody()
            .getAccessToken();
        if (accessToken == null) {
            throw new RuntimeException();
        }
        return accessToken;
    }

    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + accessToken);

        HttpEntity httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            return restTemplate
                .exchange(profileUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                .getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException();
        }
    }
}
