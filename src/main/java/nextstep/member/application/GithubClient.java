package nextstep.member.application;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
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
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<GithubAccessTokenRequest> httpEntity = new HttpEntity(githubAccessTokenRequest, headers);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate
                .exchange(tokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                .getBody()
                .getAccessToken();
    }

    public GithubProfileResponse requestGithubProfile(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format("Bearer %s", token));
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate
                .exchange(profileUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                .getBody();
    }
}
