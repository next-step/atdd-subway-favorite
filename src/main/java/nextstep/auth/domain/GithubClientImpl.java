package nextstep.auth.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static nextstep.common.constants.ErrorConstant.INVALID_AUTHENTICATION_INFO;

@Component
public class GithubClientImpl implements Oauth2Client {

    private final String clientId;
    private final String clientSecret;
    private final String tokenUrl;
    private final String profileUrl;

    public GithubClientImpl(@Value("${github.client.id}") final String clientId,
                            @Value("${github.client.secret}") final String clientSecret,
                            @Value("${github.url.access-token}") final String tokenUrl,
                            @Value("${github.url.profile}") final String profileUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenUrl = tokenUrl;
        this.profileUrl = profileUrl;
    }

    @Override
    public String getAccessToken(final String code) {
        final GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
                code,
                clientId,
                clientSecret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
                githubAccessTokenRequest, headers);
        RestTemplate restTemplate = new RestTemplate();

        final String accessToken = restTemplate
                .exchange(tokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                .getBody()
                .getAccessToken();

        if (accessToken == null) {
            throw new RuntimeException(INVALID_AUTHENTICATION_INFO);
        }

        return accessToken;
    }
}
