package nextstep.auth.application;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
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
    @Value("${github.client.id}")
    private String githubClientId;

    @Value("${github.client.secret}")
    private String githubClientSecret;

    @Value("${github.client.access-token-url}")
    private String accessTokenUrl;

    @Value("${github.client.profile-url}")
    private String profileUrl;

    public String requestGithubToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
            code,
            githubClientId,
            githubClientSecret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
            githubAccessTokenRequest, headers);
        RestTemplate restTemplate = new RestTemplate();

        String accessToken = restTemplate
            .exchange(accessTokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
            .getBody()
            .getAccessToken();

        return accessToken;
    }

    public GithubProfileResponse requestGithubProfile(String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "bearer " + accessToken);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        final HttpEntity httpEntity = new HttpEntity(headers);

        final RestTemplate restTemplate = new RestTemplate();

        return restTemplate
            .exchange(profileUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
            .getBody();
    }
}
