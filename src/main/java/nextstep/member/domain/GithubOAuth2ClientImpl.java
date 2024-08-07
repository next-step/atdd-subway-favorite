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

@Component
public class GithubOAuth2ClientImpl implements GithubOAuth2Client {

    private final RestTemplate restTemplate;

    private final GithubOAuthProperty githubOAuthProperty;

    public GithubOAuth2ClientImpl(final RestTemplate restTemplate, final GithubOAuthProperty githubOAuthProperty) {
        this.restTemplate = restTemplate;
        this.githubOAuthProperty = githubOAuthProperty;
    }

    @Override
    public AccessTokenResponse getAccessToken(final AccessTokenRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<AccessTokenRequest> httpEntity = new HttpEntity<>(request, headers);
        return restTemplate.exchange(githubOAuthProperty.getTokenUrl(), HttpMethod.POST, httpEntity, AccessTokenResponse.class)
                .getBody();
    }

    @Override
    public GithubUserInfoResponse getUserInfo(final String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", String.format("Bearer %s", accessToken));
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(githubOAuthProperty.getUserInfoUrl(), HttpMethod.GET, httpEntity, GithubUserInfoResponse.class)
                .getBody();
    }

}
