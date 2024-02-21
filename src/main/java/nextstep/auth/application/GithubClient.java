package nextstep.auth.application;

import nextstep.auth.application.dto.*;
import nextstep.exception.AuthenticationException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class GithubClient {

    private final GithubClientProperties githubClientProperties;
    private final GithubUrlProperties githubUrlProperties;
    private final RestTemplate restTemplate;

    public GithubClient(GithubClientProperties githubClientProperties, GithubUrlProperties githubUrlProperties, RestTemplate restTemplate) {
        this.githubClientProperties = githubClientProperties;
        this.githubUrlProperties = githubUrlProperties;
        this.restTemplate = restTemplate;
    }

    public GithubAccessTokenResponse requestAccessToken(String code) {
        validateCodeIsBlank(code);

        GithubAccessTokenRequest request = GithubAccessTokenRequest.of(code, githubClientProperties.getId(), githubClientProperties.getSecret());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(request, headers);

        String url = githubUrlProperties.getAccessToken();

        return Optional.ofNullable(restTemplate
                        .exchange(url, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                        .getBody())
                .orElseThrow(() -> new AuthenticationException("토큰 정보를 가져오는데 실패했습니다."));
    }

    private void validateCodeIsBlank(String code) {
        if(code == null || code.isEmpty()) {
            throw new AuthenticationException("토큰 정보를 가져오는데 실패했습니다.");
        }
    }

    public GithubEmailResponse requestGithubEmail(String accessToken) {
        validateAccessTokenIsBlank(accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add(AUTHORIZATION, "token " + accessToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(headers);

        String url = githubUrlProperties.getEmail();

        List<GithubEmailResponse> responses = restTemplate
                .exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<GithubEmailResponse>>() {})
                .getBody();

        return responses.stream()
                .filter(GithubEmailResponse::isVerifiedPrimaryEmail)
                .findFirst()
                .orElseThrow(() -> new AuthenticationException("이메일 정보를 가져오는데 실패했습니다."));
    }

    private void validateAccessTokenIsBlank(String accessToken) {
        if(accessToken == null || accessToken.isEmpty()) {
            throw new AuthenticationException("이메일 정보를 가져오는데 실패했습니다.");
        }
    }

}
