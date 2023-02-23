package nextstep.member.infra;

import nextstep.member.auth.Auth2Client;
import nextstep.member.auth.OAuth2User;
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
public class GithubClient implements Auth2Client {

    private final String clientId;
    private final String clientSecret;
    private final String tokenUrl;
    private final String profileUrl;

    public GithubClient(
        @Value("${github.client.id}") String clientId,
        @Value("${github.client.secret}") String clientSecret,
        @Value("${github.url.access-token}") String tokenUrl,
        @Value("${github.url.profile}") String profileUrl
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenUrl = tokenUrl;
        this.profileUrl = profileUrl;
    }

    @Override
    public String getAccessToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
            code,
            clientId,
            clientSecret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
            githubAccessTokenRequest,
            headers
        );
        RestTemplate restTemplate = new RestTemplate();

        String accessToken = restTemplate
            .exchange(
                tokenUrl,
                HttpMethod.POST,
                httpEntity,
                GithubAccessTokenResponse.class
            )
            .getBody()
            .getAccessToken();
        if (accessToken == null) {
            throw new RuntimeException();
        }
        return accessToken;
    }

    @Override
    public OAuth2User loadUser(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + accessToken);

        HttpEntity httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            return restTemplate
                .exchange(
                    profileUrl,
                    HttpMethod.GET,
                    httpEntity,
                    GithubProfileResponse.class
                )
                .getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException();
        }
    }
}
