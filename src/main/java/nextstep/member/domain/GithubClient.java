package nextstep.member.domain;

import lombok.RequiredArgsConstructor;
import nextstep.exception.AuthenticationException;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
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

    public String getAccessTokenFromGithub(String code) {
        HttpEntity<GithubAccessTokenRequest> httpEntity = createHttpEntityForGithubAccessTokenRequest(code);

        try {
            return restTemplate
                    .exchange(tokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                    .getBody()
                    .getAccessToken();
        } catch (HttpClientErrorException e) {
            throw new AuthenticationException("Failed to get profile from Github", e);
        }
    }

    private HttpEntity<GithubAccessTokenRequest> createHttpEntityForGithubAccessTokenRequest(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
                code,
                clientId,
                clientSecret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        return new HttpEntity<>(githubAccessTokenRequest, headers);
    }

    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        HttpEntity httpEntity = createHttpEntityForGithubProfileRequest(accessToken);

        try {
            return restTemplate
                    .exchange(profileUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                    .getBody();
        } catch (HttpClientErrorException e) {
            throw new AuthenticationException("Failed to get profile from Github", e);
        }
    }

    private HttpEntity createHttpEntityForGithubProfileRequest(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "token " + accessToken);

        HttpEntity httpEntity = new HttpEntity<>(headers);
        return httpEntity;
    }

}