package nextstep.member.application;

import nextstep.exception.FailIssueAccessTokenException;
import nextstep.member.application.request.github.GithubAccessTokenRequest;
import nextstep.member.application.response.github.GithubAccessTokenResponse;
import nextstep.member.application.response.github.GithubProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubClient {

    @Value("${github.url.access-token}")
    private String tokenUrl;
    @Value("${github.url.profile}")
    private String profileUrl;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;

    public String requestGithubToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = GithubAccessTokenRequest.of(
                code,
                clientId,
                clientSecret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(githubAccessTokenRequest, headers);
        RestTemplate restTemplate = new RestTemplate();

        GithubAccessTokenResponse githubAccessTokenResponse = restTemplate
                .exchange(tokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                .getBody();

        validateResponse(githubAccessTokenResponse.getAccessToken());

        return githubAccessTokenResponse.getAccessToken();
    }

    public GithubProfileResponse requestGithubResource(String githubAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", "bearer " + githubAccessToken);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();

        GithubProfileResponse githubProfileResponse = restTemplate
                .exchange(profileUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                .getBody();

        validateResponse(githubProfileResponse.getEmail());

        return githubProfileResponse;
    }

    private void validateResponse(String parameter) {
        if (parameter == null || parameter.isBlank()) {
            throw new FailIssueAccessTokenException();
        }
    }

}
