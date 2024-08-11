package nextstep.auth.application;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.ProfileResponse;
import nextstep.auth.exception.ApiCallException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static nextstep.common.constant.ErrorCode.GITHUB_NOT_FOUND;

@Component
public class GithubClient implements ClientRequester {
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

    @Override
    public String requestAccessToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = createGithubAccessTokenRequest(code);
        HttpHeaders headers = createGithubAccessTokenHttpHeaders();

        HttpEntity<GithubAccessTokenResponse> httpEntity = new HttpEntity(
                githubAccessTokenRequest, headers);

        ResponseEntity<GithubAccessTokenResponse> responseEntity = performApiCallGetGithubAccessToken(httpEntity);
        return extractAccessTokenFromResponse(responseEntity);
    }

    public GithubAccessTokenRequest createGithubAccessTokenRequest(String code) {
        return new GithubAccessTokenRequest(
                code,
                githubClientId,
                githubClientSecret
        );
    }

    public HttpHeaders createGithubAccessTokenHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private ResponseEntity<GithubAccessTokenResponse> performApiCallGetGithubAccessToken(HttpEntity<GithubAccessTokenResponse> httpEntity) {
        try {
            return restTemplate.postForEntity(githubAccessTokenUrl, httpEntity, GithubAccessTokenResponse.class);
        } catch (Exception e) {
            throw new ApiCallException("GITHUB_NOT_FOUND : Unexpected error occurred");
        }
    }

    private String extractAccessTokenFromResponse(ResponseEntity<GithubAccessTokenResponse> responseEntity) {
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new ApiCallException("GITHUB_NOT_FOUND : Unexpected status");
        }

        GithubAccessTokenResponse body = responseEntity.getBody();
        if (body == null || body.getAccessToken() == null) {
            throw new ApiCallException("GITHUB_NOT_FOUND : AccessToken is null");
        }

        return body.getAccessToken();
    }

    @Override
    public ProfileResponse requestProfile(String accessToken) {
        HttpHeaders headers = createRequestGithubProfileHttpHeaders(accessToken);
        HttpEntity<ProfileResponse> httpEntity = new HttpEntity(
                headers);
        ResponseEntity<ProfileResponse> responseEntity = performApiCallGetGithubProfile(httpEntity);

        return extractGithubProfilesFromResponse(responseEntity);
    }

    public HttpHeaders createRequestGithubProfileHttpHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, BEARER_ + accessToken);

        return headers;
    }

    private ResponseEntity<ProfileResponse> performApiCallGetGithubProfile(HttpEntity<ProfileResponse> httpEntity) {
        try {
            return restTemplate
                    .exchange(githubAccessProfileUrl, HttpMethod.GET, httpEntity, ProfileResponse.class);
        } catch (Exception e) {
            throw new ApiCallException(GITHUB_NOT_FOUND + " : Unexpected error occurred");
        }
    }

    private ProfileResponse extractGithubProfilesFromResponse(ResponseEntity<ProfileResponse> responseEntity) {
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new ApiCallException(GITHUB_NOT_FOUND + " : Unexpected status");
        }

        if (responseEntity.getBody() == null) {
            throw new ApiCallException(GITHUB_NOT_FOUND + " : Profile is null");
        }

        return responseEntity.getBody();
    }

}

