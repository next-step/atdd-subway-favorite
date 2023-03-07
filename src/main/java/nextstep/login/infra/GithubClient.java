package nextstep.login.infra;

import nextstep.login.application.dto.request.GithubAccessTokenRequest;
import nextstep.login.application.dto.response.GithubAccessTokenResponse;
import nextstep.login.application.dto.response.GithubProfileResponse;
import nextstep.login.application.exception.FailAuthorizationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    /**
     * Github 인증 서버에 사용자 인증 토큰(Access Token)을 요청합니다.
     *
     * @param code 사용자 권한 증서
     * @return 사용자 인증 토큰
     */
    @Override
    public String getAccessTokenFromGithub(final String code) {
        GithubAccessTokenResponse response = httpRequestToGithubAuthServer(code);

        return Optional.ofNullable(response.getAccessToken())
                .orElseThrow(FailAuthorizationException::new);
    }

    private GithubAccessTokenResponse httpRequestToGithubAuthServer(final String code) {
        try {
            HttpEntity<MultiValueMap<String, String>> httpEntity = createEntityForGithubAccessTokenRequest(code);

            return Optional.ofNullable(
                            httpRequest(tokenUrl, POST, httpEntity, GithubAccessTokenResponse.class)
                                    .getBody())
                    .orElseThrow(FailAuthorizationException::new);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("HTTP 요청이 실패하였습니다." + e.getMessage());
        }
    }

    private HttpEntity<MultiValueMap<String, String>> createEntityForGithubAccessTokenRequest(final String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(code, clientId, clientSecret);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        return new HttpEntity(githubAccessTokenRequest, headers);
    }

    private <T> ResponseEntity<T> httpRequest(
            final String url,
            final HttpMethod method,
            final HttpEntity<MultiValueMap<String, String>> httpEntity,
            final Class<T> responseType
    ) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.exchange(url, method, httpEntity, responseType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("HTTP 요청이 실패하였습니다." + e.getMessage());
        }
    }

    /**
     * Github의 프로필 정보를 조회합니다.
     *
     * @param accessToken 사용자 인증 토큰
     * @return Github 프로필 정보
     */
    @Override
    public GithubProfileResponse getGithubProfileFromGithub(final String accessToken) {
        return httpRequestToGithubResourceServer(accessToken);
    }

    private GithubProfileResponse httpRequestToGithubResourceServer(final String accessToken) {
        try {
            HttpEntity<MultiValueMap<String, String>> httpEntity = createEntityForGithubProfileRequest(accessToken);

            return Optional.ofNullable(
                            httpRequest(profileUrl, GET, httpEntity, GithubProfileResponse.class)
                                    .getBody())
                    .orElseThrow(FailAuthorizationException::new);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("HTTP 요청이 실패하였습니다." + e.getMessage());
        }
    }

    private HttpEntity<MultiValueMap<String, String>> createEntityForGithubProfileRequest(final String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + accessToken);

        return new HttpEntity(headers);
    }
}
