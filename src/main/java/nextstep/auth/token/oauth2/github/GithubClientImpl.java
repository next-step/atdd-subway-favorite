package nextstep.auth.token.oauth2.github;

import lombok.RequiredArgsConstructor;
import nextstep.auth.AuthenticationException;
import nextstep.auth.ForbiddenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GithubClientImpl implements GithubClient{

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.url.access-token}")
    private String tokenUrl;
    @Value("${github.url.profile}")
    private String profileUrl;

    @Override
    public String getAccessTokenFromGithub(String code) throws AuthenticationException {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
            code,
            clientId,
            clientSecret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
            githubAccessTokenRequest, headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new GetAccessTokenErrorHandler());

        try {
            String accessToken = restTemplate
                    .exchange(tokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                    .getBody()
                    .getAccessToken();
            if (accessToken == null) {
                new AuthenticationException("auth.0001");
            }
            return accessToken;
        } catch (HttpClientErrorException e) {
            throw new AuthenticationException("auth.0001");
        }
    }

    @Override
    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) throws ForbiddenException{
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new GetGithubProfileErrorHandler());

        try {
            return restTemplate
                .exchange(profileUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                .getBody();
        } catch (HttpClientErrorException e) {
            throw new ForbiddenException("auth.0002");
        }
    }
}
