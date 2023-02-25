package nextstep.member.application;

import java.util.Optional;
import nextstep.member.application.config.GithubProperties;
import nextstep.member.application.dto.github.GithubAccessTokenRequest;
import nextstep.member.application.dto.github.GithubAccessTokenResponse;
import nextstep.member.application.dto.github.GithubProfileResponse;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

    public Optional<String> getAccessTokenFromGithub(String code) {
        var client = githubProperties.getClient();
        var url = githubProperties.getUrl();

        GithubAccessTokenRequest body = new GithubAccessTokenRequest(code, client.getId(), client.getSecret());
        GithubAccessTokenResponse response = restTemplate.postForObject(url.getAccessToken(), body, GithubAccessTokenResponse.class);
        if (response == null) {
            throw new IllegalStateException("올바른 응답이 아닙니다.");
        }

        return Optional.ofNullable(response.getAccessToken());
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
            throw new IllegalStateException(e);
        }
    }
}
