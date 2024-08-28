package nextstep.member.application;

import nextstep.member.application.dto.*;
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
public class GithubClient implements TokenClient {
    @Value("${security.github.token.client-id}")
    private String clientId;
    @Value("${security.github.token.client-secret}")
    private String clientSecret;
    @Value("${security.github.access-token-uri}")
    private String accessTokenURI;
    @Value("${security.github.access-profile-uri}")
    private String profileURI;

    @Override
    public String requestAccessToken(String code) {
        OAuthAccessTokenRequest githubAccessTokenRequest = new OAuthAccessTokenRequest(
                code, clientId, clientSecret);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
                githubAccessTokenRequest, createHeader());
        RestTemplate restTemplate = new RestTemplate();

        return Objects.requireNonNull(restTemplate
                        .exchange(accessTokenURI, HttpMethod.POST, httpEntity, TokenResponse.class)
                        .getBody())
                .getAccessToken();
    }

    @Override
    public OAuthProfileResponse requestUserProfile(String accessToken) {
        HttpHeaders headers = createHeader();
        headers.add("Authorization", accessToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
                accessToken, headers);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate
                .exchange(profileURI, HttpMethod.GET, httpEntity, OAuthProfileResponse.class)
                .getBody();
    }


    public HttpHeaders createHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

}
