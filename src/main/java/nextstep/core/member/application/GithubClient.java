package nextstep.core.member.application;

import nextstep.core.member.application.dto.GithubAccessTokenRequest;
import nextstep.core.member.application.dto.GithubAccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubClient {

    @Value("${github.base-url}")
    private String baseUrl;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    public GithubProfileResponse requestGithubProfile(String code) {
        return requestGithubUserInfo(requestGithubToken(code));
    }

    private GithubProfileResponse requestGithubUserInfo(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, accessToken);

        return new RestTemplate()
                .exchange(baseUrl + "user",
                        HttpMethod.GET,
                        new HttpEntity<>(httpHeaders),
                        GithubProfileResponse.class)
                .getBody();
    }

    private String requestGithubToken(String code) {
        HttpHeaders accessTokenRequestHeaders = new HttpHeaders();
        accessTokenRequestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        return new RestTemplate()
                .exchange(baseUrl + "login/oauth/access_token",
                        HttpMethod.POST,
                        new HttpEntity<>(createAccessTokenRequest(code), accessTokenRequestHeaders),
                        GithubAccessTokenResponse.class)
                .getBody()
                .getAccessToken();
    }

    private GithubAccessTokenRequest createAccessTokenRequest(String code) {
        return new GithubAccessTokenRequest(
                code,
                clientId,
                clientSecret
        );
    }
}
