package nextstep.subway.auth.client.github;

import nextstep.subway.auth.client.github.config.GithubClientProperties;
import nextstep.subway.auth.client.github.dto.GithubAccessTokenRequest;
import nextstep.subway.auth.client.github.dto.GithubAccessTokenResponse;
import nextstep.subway.auth.client.github.dto.GithubProfileResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class GithubClient {

    public static final String ACCESS_TOKEN_PATH = "/github/login/oauth/access_token";
    public static final String FIND_USER_PATH = "/github/user";
    private final GithubClientProperties githubClientProperties;
    private final RestTemplate restTemplate;

    public GithubClient(GithubClientProperties githubClientProperties) {
        this.githubClientProperties = githubClientProperties;
        restTemplate = new RestTemplateBuilder()
                .rootUri(githubClientProperties.getRootUrl())
                .messageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    public String requestToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
                code,
                githubClientProperties.getClientId(),
                githubClientProperties.getClientSecret()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(githubAccessTokenRequest, headers);

        return Optional.ofNullable(restTemplate
                        .exchange(ACCESS_TOKEN_PATH, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                        .getBody())
                .map(GithubAccessTokenResponse::getAccessToken)
                .orElseThrow(() -> new IllegalArgumentException("토큰 정보를 가지고 오지 못했습니다."));
    }

    public GithubProfileResponse findUser(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(headers);

        return Optional.ofNullable(restTemplate
                        .exchange(FIND_USER_PATH, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                        .getBody())
                .orElseThrow(() -> new IllegalArgumentException("토큰 정보를 가지고 오지 못했습니다."));
    }
}
