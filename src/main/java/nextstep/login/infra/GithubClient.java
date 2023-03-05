package nextstep.login.infra;

import nextstep.login.application.dto.request.GithubAccessTokenRequest;
import nextstep.login.application.dto.response.GithubAccessTokenResponse;
import nextstep.login.application.dto.response.GithubProfileResponse;
import nextstep.login.application.exception.FailAuthorizationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Component
public class GithubClient implements SocialClient {

    private final String clientId;
    private final String clientSecret;
    private final String tokenUrl;
    private final String profileUrl;

    public GithubClient(
            @Value("${github.client.id}") final String clientId,
            @Value("${github.client.secret}") final String clientSecret,
            @Value("${github.url.access-token}") final String tokenUrl,
            @Value("${github.url.profile}") final String profileUrl
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenUrl = tokenUrl;
        this.profileUrl = profileUrl;
    }

    @Override
    public String getAccessTokenFromGithub(final String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(code, clientId, clientSecret);
        HttpEntity<MultiValueMap<String, String>> httpEntity = createHttpEntityBody(githubAccessTokenRequest);
        GithubAccessTokenResponse response = httpRequestToGithubAuthServer(httpEntity);

        return Optional.ofNullable(response.getAccessToken())
                .orElseThrow(FailAuthorizationException::new);
    }

    private <T> HttpEntity<MultiValueMap<String, String>> createHttpEntityBody(final T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        return new HttpEntity(body, headers);
    }

    private GithubAccessTokenResponse httpRequestToGithubAuthServer(
            final HttpEntity<MultiValueMap<String, String>> httpEntity
    ) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            return Optional.ofNullable(
                            restTemplate.exchange(tokenUrl, POST, httpEntity, GithubAccessTokenResponse.class)
                                    .getBody())
                    .orElseThrow(FailAuthorizationException::new);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("HTTP 요청이 실패하였습니다." + e.getMessage());
        }
    }


    @Override
    public GithubProfileResponse getGithubProfileFromGithub(final String accessToken) {
        HttpEntity<MultiValueMap<String, String>> httpEntity = createHttpEntityHeader(accessToken);
        GithubProfileResponse githubProfileResponse = httpRequestToGithubResourceServer(httpEntity);

        return createGithubProfileResponse(githubProfileResponse);
    }

    private <T> HttpEntity<MultiValueMap<String, String>> createHttpEntityHeader(final T header) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + header);

        return new HttpEntity(headers);
    }

    private GithubProfileResponse httpRequestToGithubResourceServer(
            final HttpEntity<MultiValueMap<String, String>> httpEntity
    ) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            return Optional.ofNullable(
                            restTemplate.exchange(profileUrl, GET, httpEntity, GithubProfileResponse.class)
                                    .getBody())
                    .orElseThrow(FailAuthorizationException::new);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("HTTP 요청이 실패하였습니다." + e.getMessage());
        }
    }

    private GithubProfileResponse createGithubProfileResponse(final GithubProfileResponse githubProfileResponse) {
        String email = Optional.ofNullable(githubProfileResponse.getEmail())
                .orElseThrow(FailAuthorizationException::new);

        return new GithubProfileResponse(email);
    }
}
