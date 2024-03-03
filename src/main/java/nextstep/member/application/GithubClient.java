package nextstep.member.application;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


public class GithubClient implements OAuth2Client {

    private String clientId;

    private String clientSecret;

    private String accessTokenUri;

    public GithubClient(String clientId, String clientSecret, String accessTokenUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessTokenUri = accessTokenUri;
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
}
