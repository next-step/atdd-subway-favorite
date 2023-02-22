package nextstep.member.infrastructure;

import nextstep.member.infrastructure.dto.GithubTokenRequest;
import nextstep.member.infrastructure.dto.GithubTokenResponse;
import nextstep.member.infrastructure.dto.GithubProfileResponse;
import nextstep.member.infrastructure.exception.InternalServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.Option;
import java.util.Optional;

@Component
public class GithubClientImpl implements GithubClient {

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.url.access-token}")
    private String tokenUrl;
    @Value("${github.url.profile}")
    private String profileUrl;

    public Optional<String> getAccessToken(String code) {
        GithubTokenRequest githubTokenRequest = new GithubTokenRequest(
                code,
                clientId,
                clientSecret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity httpEntity = new HttpEntity(githubTokenRequest, headers);
        RestTemplate restTemplate = new RestTemplate();

        String accessToken = restTemplate
                .exchange(tokenUrl, HttpMethod.POST, httpEntity, GithubTokenResponse.class)
                .getBody()
                .getAccessToken();

        return Optional.ofNullable(accessToken);
    }

    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + accessToken);

        HttpEntity httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            return restTemplate
                    .exchange(profileUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                    .getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException();
        }
    }
}
