package nextstep.member.domain;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class GithubClient {

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.url.access-token}")
    private String tokenUrl;
    @Value("${github.url.profile}")
    private String profileUrl;

    private final RestTemplate restTemplate;

    public String getAccessTokenFromGithub(String code) {
        HttpEntity<MultiValueMap<String, String>> httpEntity = createGithubAccessTokenRequestHttpEntity(code);

        GithubAccessTokenResponse response = restTemplate
                .exchange(tokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                .getBody();

        String accessToken = Optional.ofNullable(response)
                .orElseThrow(RuntimeException::new)
                .getAccessToken();

        if (accessToken == null) {
            throw new RuntimeException();
        }
        return accessToken;
    }

    private HttpEntity<MultiValueMap<String, String>> createGithubAccessTokenRequestHttpEntity(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
                code,
                clientId,
                clientSecret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(githubAccessTokenRequest, headers);
        return httpEntity;
    }

    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        HttpEntity httpEntity = createGithubProfileRequestHttpEntity(accessToken);

        try {
            return restTemplate
                    .exchange(profileUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                    .getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException();
        }
    }

    private static HttpEntity createGithubProfileRequestHttpEntity(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "token " + accessToken);

        HttpEntity httpEntity = new HttpEntity<>(headers);
        return httpEntity;
    }

}