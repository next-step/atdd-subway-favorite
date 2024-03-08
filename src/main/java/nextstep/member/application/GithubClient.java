package nextstep.member.application;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubClient {
    @Value("${oauth2.github.login-url}")
    private String url;

    @Value("${oauth2.github.user-url}")
    private String userInfoUrl;

    public String requestGithubToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
            code,
            "clientId", // client id
            "clientSecret" // client secret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
            githubAccessTokenRequest, headers);
        RestTemplate restTemplate = new RestTemplate();

        String accessToken = restTemplate
            .exchange(url, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
            .getBody()
            .getAccessToken();

        return accessToken;
    }

    public GithubProfileResponse findUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", accessToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate
            .exchange(userInfoUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
            .getBody();

    }
}
