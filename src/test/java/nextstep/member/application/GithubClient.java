package nextstep.member.application;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class GithubClient {
    public String requestGithubToken(final String code) {
        final GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
                code,
                "clientId", // client id
                "clientSecret" // client secret
        );

        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        final HttpEntity<GithubAccessTokenRequest> httpEntity = new HttpEntity<>(githubAccessTokenRequest, headers);
        final RestTemplate restTemplate = new RestTemplate();

        final String url = "http://localhost:8080/github/login/oauth/access_token";

        return restTemplate
                .exchange(url, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                .getBody()
                .getAccessToken();
    }
}
