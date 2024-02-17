package nextstep.member.application;

import nextstep.exception.ApplicationException;
import nextstep.member.application.dto.*;
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

    public GithubClient(GithubClientProperties githubClientProperties, GithubUrlProperties githubUrlProperties) {
        this.githubClientProperties = githubClientProperties;
        this.githubUrlProperties = githubUrlProperties;
    }

    public GithubAccessTokenResponse requestAccessToken(String code) {
        GithubAccessTokenRequest request = GithubAccessTokenRequest.of(code, githubClientProperties.getId(), githubClientProperties.getSecret());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(request, headers);
        RestTemplate restTemplate = new RestTemplate();

        String url = githubUrlProperties.getAccessToken();

        return Optional.ofNullable(restTemplate
                        .exchange(url, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                        .getBody())
                .orElseThrow(() -> new ApplicationException("토큰을 가져오는데 실패했습니다."));
    }

    public GithubEmailResponse requestGithubEmail(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add(AUTHORIZATION, "token " + accessToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();

        String url = githubUrlProperties.getAccessToken();

        List<GithubEmailResponse> responses = restTemplate
                .exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<GithubEmailResponse>>() {})
                .getBody();

        return responses.stream()
                .filter(GithubEmailResponse::isVerifiedPrimaryEmail)
                .findFirst()
                .orElseThrow(() -> new ApplicationException("이메일을 가져오는데 실패했습니다."));
    }

}
