package nextstep.member.application;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
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

    @Value("${security.github.client.id}")
    private String clientId;
    @Value("${security.github.client.secret}")
    private String clientSecret;
    @Value("${security.github.client.access-token-uri}")
    private String accessTokenURI;

    public String requestGithubAeccessToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
            code, clientId, clientSecret);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
            githubAccessTokenRequest, createHeaders());

        return getGithubAccessToken(new RestTemplate(), httpEntity, accessTokenURI);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private String getGithubAccessToken(RestTemplate restTemplate, HttpEntity<MultiValueMap<String, String>> httpEntity, String url) {
        return restTemplate
            .exchange(url, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
            .getBody()
            .getAccessToken();
    }
}
