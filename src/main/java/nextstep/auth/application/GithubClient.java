package nextstep.auth.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.GithubResponses;
import nextstep.auth.dto.GithubAccessTokenRequest;
import nextstep.auth.dto.GithubAccessTokenResponse;
import nextstep.auth.dto.GithubProfileResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class GithubClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String GITHUB_URL = "http://localhost:8080/github/login/oauth/access_token";
    private final String GITHUB_USER_URL = "http://localhost:8080/github/user";
    public String getAccessTokenFromGithub(String code) {
        String clientId = GithubResponses.getEmail(code);
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
                code,
                clientId, // client id
                "clientSecret" // client secret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        try {
            ResponseEntity<GithubAccessTokenResponse> responseEntity = restTemplate
                    .postForEntity(GITHUB_URL, githubAccessTokenRequest, GithubAccessTokenResponse.class);
            return Objects.requireNonNull(responseEntity.getBody()).getAccessToken();
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new AuthenticationException();
        }
    }

    public GithubProfileResponse getUserProfile(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        try {
            ResponseEntity<GithubProfileResponse> responseEntity = restTemplate
                    .exchange(GITHUB_USER_URL, HttpMethod.GET, new HttpEntity<>(headers), GithubProfileResponse.class);
            return Objects.requireNonNull(responseEntity.getBody());
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new AuthenticationException();
        }
    }

}
