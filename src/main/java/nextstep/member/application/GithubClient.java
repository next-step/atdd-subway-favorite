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

import java.util.Objects;

@Component
public class GithubClient {
    @Value("${security.github.token.client-id}")
    private String clientId;
    @Value("${security.github.token.client-secret}")
    private String clientSecret;
    @Value("${security.github.access-token-uri}")
    private String accessTokenURI;
    @Value("${security.github.access-profile-uri}")
    private String profileURI;

    public String requestGithubAeccessToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
                code, clientId, clientSecret);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
                githubAccessTokenRequest, createHeader());
        RestTemplate restTemplate = new RestTemplate();

        return Objects.requireNonNull(restTemplate
                        .exchange(accessTokenURI, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                        .getBody())
                .getAccessToken();
    }

    public GithubProfileResponse requestGithubUser(String githubAccessToken) {
        HttpHeaders headers = createHeader();
        headers.add("Authorization", githubAccessToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
                githubAccessToken, headers);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate
                .exchange(profileURI, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                .getBody();
    }
    public HttpHeaders createHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
