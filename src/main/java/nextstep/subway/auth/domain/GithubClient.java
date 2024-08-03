package nextstep.subway.auth.domain;

import nextstep.subway.auth.application.dto.GithubAccessTokenRequest;
import nextstep.subway.auth.application.dto.GithubAccessTokenResponse;
import nextstep.subway.auth.application.dto.GithubProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
public class GithubClient {
    @Value("${github.client.id}")
    private String client;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.url.token}")
    private String githubUrlToken;

    @Value("${github.url.user.profile}")
    private String githubUrlUserProfile;

    public String requestGithubToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(code, client, clientSecret);

        HttpEntity<GithubAccessTokenRequest> httpEntity = new HttpEntity(githubAccessTokenRequest, makeJsonHeader());
        RestTemplate restTemplate = new RestTemplate();

        String url = githubUrlToken;
        String accessToken = getGithubAccessToken(restTemplate, url, httpEntity);

        return accessToken;
    }

    public GithubProfileResponse requestGithubUserInfo(String githubAccessToken) {
        HttpHeaders jsonHeaders = makeJsonHeader();
        jsonHeaders.setBearerAuth(githubAccessToken);

        HttpEntity<GithubAccessTokenRequest> httpEntity = new HttpEntity(jsonHeaders);
        RestTemplate restTemplate = new RestTemplate();

        String url = githubUrlUserProfile;
        GithubProfileResponse userProfile = getUserProfile(restTemplate, url, httpEntity);

        return userProfile;
    }

    private HttpHeaders makeJsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private GithubProfileResponse getUserProfile(RestTemplate restTemplate, String url, HttpEntity<GithubAccessTokenRequest> httpEntity) {
        return restTemplate
                .exchange(url, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                .getBody();
    }

    private String getGithubAccessToken(RestTemplate restTemplate, String url, HttpEntity<GithubAccessTokenRequest> httpEntity) {
        return restTemplate
                .exchange(url, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                .getBody()
                .getAccessToken();
    }
}
