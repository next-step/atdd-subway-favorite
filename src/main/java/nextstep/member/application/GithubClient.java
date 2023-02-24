package nextstep.member.application;

import nextstep.member.application.config.GithubProperties;
import nextstep.member.application.dto.github.GithubAccessTokenRequest;
import nextstep.member.application.dto.github.GithubAccessTokenResponse;
import nextstep.member.application.dto.github.GithubProfileResponse;
import nextstep.member.domain.exception.NotAuthorizedException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@EnableConfigurationProperties(GithubProperties.class)
@Component
public class GithubClient {

    private final GithubProperties githubProperties;
    private final RestTemplate restTemplate;

    public GithubClient(GithubProperties githubProperties, RestTemplate restTemplate) {
        this.githubProperties = githubProperties;
        this.restTemplate = restTemplate;
    }

    public String getAccessTokenFromGithub(String code) {
        var client = githubProperties.getClient();
        var url = githubProperties.getUrl();

        GithubAccessTokenRequest body = new GithubAccessTokenRequest(code, client.getId(), client.getSecret());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<GithubAccessTokenRequest> httpEntity = new HttpEntity<>(body, headers);
        String accessToken = restTemplate
            .exchange(url.getAccessToken(), HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
            .getBody()
            .getAccessToken();

        if (accessToken == null) {
            throw new NotAuthorizedException("인증정보가 유효하지 않습니다.");
        }
        return accessToken;
    }

    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        var url = githubProperties.getUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "token " + accessToken);

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        try {
            return restTemplate
                .exchange(url.getProfile(), HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                .getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException();
        }
    }
}
