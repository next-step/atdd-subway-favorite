package nextstep.member.domain;

import nextstep.member.payload.AccessTokenRequest;
import nextstep.member.payload.AccessTokenResponse;
import nextstep.member.payload.GithubUserInfoResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class GithubOAuth2ClientImpl implements GithubOAuth2Client {

    private final RestTemplate restTemplate;

    private final GithubOAuthProperty githubOAuthProperty;

    public GithubOAuth2ClientImpl(final RestTemplate restTemplate, final GithubOAuthProperty githubOAuthProperty) {
        this.restTemplate = restTemplate;
        this.githubOAuthProperty = githubOAuthProperty;
    }

    @Override
    public AccessTokenResponse getAccessToken(final String code) {
        AccessTokenRequest request = AccessTokenRequest.builder()
            .clientId(githubOAuthProperty.getClientId())
            .clientSecret(githubOAuthProperty.getClientSecret())
            .code(code)
            .build();
        HttpHeaders headers = createHeader(Map.of());
        HttpEntity<AccessTokenRequest> httpEntity = new HttpEntity<>(request, headers);
        return restTemplate.exchange(githubOAuthProperty.getTokenUrl(), HttpMethod.POST, httpEntity, AccessTokenResponse.class)
                .getBody();
    }

    @Override
    public GithubUserInfoResponse getUserInfo(final String accessToken) {
        HttpHeaders headers = createHeader(Map.of(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken)));
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(githubOAuthProperty.getUserInfoUrl(), HttpMethod.GET, httpEntity, GithubUserInfoResponse.class)
                .getBody();
    }

    private HttpHeaders createHeader(Map<String, String> additionalHeaders) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        additionalHeaders.forEach(headers::add);
        return headers;
    }

}
