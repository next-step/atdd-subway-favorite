package nextstep.auth.infra;

import nextstep.auth.AuthenticationException;
import nextstep.auth.infra.dto.GithubAccessTokenRequest;
import nextstep.auth.infra.dto.GithubAccessTokenResponse;
import nextstep.auth.infra.dto.GithubProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class GithubClient {

    @Value("${github.client.id}")
    private String id;
    @Value("${github.client.secret}")
    private String secret;
    @Value("${github.url.access-token}")
    private String accessTokenUrl;
    @Value("${github.url.profile}")
    private String profileUrl;


    private final RestTemplate restTemplate = new RestTemplate();

    public String getAccessTokenFromGithub(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(code, id, secret);

        try {
            ResponseEntity<GithubAccessTokenResponse> responseEntity = restTemplate
                    .postForEntity(accessTokenUrl, githubAccessTokenRequest, GithubAccessTokenResponse.class);
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
                    .exchange(profileUrl, HttpMethod.GET, new HttpEntity<>(headers), GithubProfileResponse.class);
            return Objects.requireNonNull(responseEntity.getBody());
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new AuthenticationException();
        }
    }

}
