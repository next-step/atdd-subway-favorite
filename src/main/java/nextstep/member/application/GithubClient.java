package nextstep.member.application;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.exception.ApiCallException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
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
    private final String BEARER_ = "Bearer ";

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
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<GithubAccessTokenResponse> httpEntity = new HttpEntity(
                githubAccessTokenRequest, headers);
        ResponseEntity<GithubAccessTokenResponse> responseEntity;

        try {
            responseEntity = restTemplate
                    .postForEntity(githubAccessTokenUrl, httpEntity, GithubAccessTokenResponse.class);

        } catch (Exception e) {
            throw new ApiCallException(GITHUB_NOT_FOUND + " : Unexpected error occurred");
        }

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new ApiCallException(GITHUB_NOT_FOUND + " : Unexpected status");
        }

        if (responseEntity.getBody() == null || responseEntity.getBody().getAccessToken() == null) {
            throw new ApiCallException(GITHUB_NOT_FOUND + " : AccessToken is null");
        }

        return responseEntity.getBody().getAccessToken();
    }

    public GithubProfileResponse requestGithubProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, BEARER_ +accessToken);

        HttpEntity<GithubProfileResponse> httpEntity = new HttpEntity(
                headers);
        ResponseEntity<GithubProfileResponse> responseEntity;

        try {
            responseEntity = restTemplate
                    .exchange(githubAccessProfileUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class);
        } catch (Exception e) {
            throw new ApiCallException(GITHUB_NOT_FOUND + " : Unexpected error occurred");
        }

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new ApiCallException(GITHUB_NOT_FOUND + " : Unexpected status");
        }

        if (responseEntity.getBody() == null) {
            throw new ApiCallException(GITHUB_NOT_FOUND + " : Profile is null");
        }

        return responseEntity.getBody();
    }
}

