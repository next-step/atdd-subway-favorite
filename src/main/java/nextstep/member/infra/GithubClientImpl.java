package nextstep.member.infra;

import lombok.RequiredArgsConstructor;
import nextstep.member.infra.dto.GithubAccessTokenRequest;
import nextstep.member.infra.dto.GithubAccessTokenResponse;
import nextstep.member.infra.dto.GithubProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GithubClientImpl implements GithubClient {

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.url.access-token}")
    private String tokenUrl;
    @Value("${github.url.profile}")
    private String profileUrl;
    private final RestTemplate restTemplate;

    @Override
    public String getAccessTokenFromGithub(final String code) {
        final GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
                code,
                clientId,
                clientSecret
        );

        final HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        final HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(githubAccessTokenRequest, headers);

        final String accessToken = restTemplate
                .exchange(tokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                .getBody()
                .getAccessToken();
        if (accessToken == null) {
            throw new RuntimeException();
        }
        return accessToken;
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(final String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + accessToken);

        final HttpEntity httpEntity = new HttpEntity<>(headers);

        try {
            return restTemplate
                    .exchange(profileUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                    .getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException();
        }
    }
}
