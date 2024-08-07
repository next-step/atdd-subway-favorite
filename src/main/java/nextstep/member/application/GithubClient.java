package nextstep.member.application;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.exception.ApiCallException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static nextstep.common.constant.ErrorCode.GITHUB_NOT_FOUND;

@Component
public class GithubClient {
    @Value("${security.github.access-token-uri}")
    private String githubAccessTokenUrl;
    @Value("${security.github.access-profile-uri}")
    private String githubAccessProfileUrl;
    @Value("${security.github.client-id}")
    private String githubClientId;
    @Value("${security.github.client-secret}")
    private String githubClientSecret;

    private final RestTemplate restTemplate;

    public GithubClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String requestGithubAccessToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
                code,
                githubClientId,
                githubClientSecret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
                githubAccessTokenRequest, headers);


        try {
            ResponseEntity<GithubAccessTokenResponse> responseEntity = restTemplate
                    .exchange(githubAccessTokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
                return responseEntity.getBody().getAccessToken();
            }

            throw new ApiCallException(GITHUB_NOT_FOUND + " : Response body is null");

        } catch (Exception e) {
            throw new ApiCallException(GITHUB_NOT_FOUND + " : Unexpected error occurred");
        }
    }

    public GithubProfileResponse requestGithubProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", accessToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
                headers);
        GithubProfileResponse githubProfileResponse;
        try {
            githubProfileResponse = restTemplate
                    .exchange(githubAccessProfileUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                    .getBody();
        } catch (Exception e) {
            throw new ApiCallException(GITHUB_NOT_FOUND + " : Unexpected error occurred");
        }

        return githubProfileResponse;
    }
}

