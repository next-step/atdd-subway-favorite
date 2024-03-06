package nextstep.auth.application;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubUserInfoRequest;
import nextstep.auth.ui.dto.GithubProfileResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


public class GithubClient implements OAuth2Client {

    private final String clientId;
    private final String clientSecret;
    private final String accessTokenUri;
    private final String userInfoUri;

    public GithubClient(String clientId, String clientSecret, String accessTokenUri,
        String userInfoUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessTokenUri = accessTokenUri;
        this.userInfoUri = userInfoUri;
    }

    @Override
    public String requestAccessToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
            code,
            clientId,
            clientSecret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
            githubAccessTokenRequest, headers);
        RestTemplate restTemplate = new RestTemplate();

        String url = accessTokenUri;
        ResponseEntity<GithubAccessTokenResponse> response = restTemplate
            .exchange(url, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class);

        return response.getBody().getAccessToken();
    }

    @Override
    public GithubProfileResponse requestUserInfo(String accessToken) {
        GithubUserInfoRequest githubUserInfoRequest = new GithubUserInfoRequest(accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
            githubUserInfoRequest, headers);

        String url = userInfoUri;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GithubProfileResponse> response = restTemplate
            .exchange(url, HttpMethod.GET, httpEntity, GithubProfileResponse.class);

        return response.getBody();
    }
}
