package nextstep.member.application;

import nextstep.member.application.dto.github.GithubAccessTokenRequest;
import nextstep.member.application.dto.github.GithubAccessTokenResponse;
import nextstep.member.application.dto.github.GithubProfileResponse;
import nextstep.member.domain.exception.NotAuthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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
        GithubAccessTokenRequest body = new GithubAccessTokenRequest(code, clientId, clientSecret);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<GithubAccessTokenRequest> httpEntity = new HttpEntity<>(body, headers);
        String accessToken = restTemplate
            .exchange(tokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
            .getBody()
            .getAccessToken();

        if (accessToken == null) {
            throw new NotAuthorizedException("인증정보가 유효하지 않습니다.");
        }
        return accessToken;
    }

    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "token " + accessToken);

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        try {
            return restTemplate
                .exchange(profileUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                .getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException();
        }
    }
}
