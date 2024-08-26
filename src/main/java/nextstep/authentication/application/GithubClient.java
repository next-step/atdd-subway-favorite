package nextstep.authentication.application;

import nextstep.authentication.application.dto.GithubAccessTokenRequest;
import nextstep.authentication.application.dto.GithubAccessTokenResponse;
import nextstep.authentication.application.dto.GithubProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubClient {

    private static final String AUTHORIZATION = "Authorization";
    private static final String ACCEPT = "Accept";
    private static final String TOKEN_FORMAT = "Bearer %s";

    @Value("${github.token.url}")
    private String tokenUrl;

    @Value("${github.profile.url}")
    private String profileUrl;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    public String requestGithubToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(code, clientId, clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.add(ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<GithubAccessTokenRequest> httpEntity = new HttpEntity<>(githubAccessTokenRequest, headers);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate
                .exchange(tokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                .getBody()
                .getAccessToken();
    }

    public GithubProfileResponse requestGithubProfile(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, String.format(TOKEN_FORMAT, token));
        headers.add(ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate
                .exchange(profileUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                .getBody();
    }
}
