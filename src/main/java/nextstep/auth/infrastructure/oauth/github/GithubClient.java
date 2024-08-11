package nextstep.auth.infrastructure.oauth.github;

import nextstep.auth.infrastructure.oauth.github.dto.GithubAccessTokenRequest;
import nextstep.auth.infrastructure.oauth.github.dto.GithubAccessTokenResponse;
import nextstep.auth.infrastructure.oauth.github.dto.GithubProfileResponse;
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

    @Value("${security.github.client.id}")
    private String clientId;
    @Value("${security.github.client.secret}")
    private String clientSecret;
    @Value("${security.github.client.access-token-uri}")
    private String accessTokenURI;
    @Value("${security.github.client.access-profile-uri}")
    private String profileURI;

    public String requestGithubAeccessToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
            code, clientId, clientSecret);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
            githubAccessTokenRequest, createHeaders());

        return getGithubAccessToken(new RestTemplate(), httpEntity, accessTokenURI);
    }

    public GithubProfileResponse requestGithubUser(String githubAccessToken) {
        HttpHeaders headers = createHeaders();
        headers.add("Authorization", githubAccessToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(githubAccessToken,
            headers);

        return getGithubUser(new RestTemplate(), httpEntity, profileURI);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private String getGithubAccessToken(RestTemplate restTemplate, HttpEntity<MultiValueMap<String, String>> httpEntity, String url) {
        return restTemplate
            .exchange(url, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
            .getBody()
            .getAccessToken();
    }

    private GithubProfileResponse getGithubUser(RestTemplate restTemplate, HttpEntity<MultiValueMap<String, String>> httpEntity, String url) {
        return restTemplate
            .exchange(url, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
            .getBody();
    }
}
