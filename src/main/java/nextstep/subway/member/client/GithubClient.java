package nextstep.subway.member.client;

import nextstep.subway.member.client.config.GithubClientProperties;
import nextstep.subway.member.client.dto.GithubAccessTokenRequest;
import nextstep.subway.member.client.dto.GithubAccessTokenResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

public class GithubClient {

    public static final String ACCESS_TOKEN = "/github/login/oauth/access_token";
    public static final String FIND_USER = "/github/user";
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
                .exchange(ACCESS_TOKEN, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                .getBody())
                .map(GithubAccessTokenResponse::getAccessToken)
                .orElseThrow(() -> new IllegalArgumentException("토큰 정보를 가지고 오지 못했습니다."));
    }

    public GithubProfileResponse findUser(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(headers);

        return Optional.ofNullable(restTemplate
                        .exchange(FIND_USER, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                        .getBody())
                .orElseThrow(() -> new IllegalArgumentException("토큰 정보를 가지고 오지 못했습니다."));
    }
}