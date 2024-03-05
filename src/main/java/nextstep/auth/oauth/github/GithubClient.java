package nextstep.auth.oauth.github;

import nextstep.common.properties.GithubConfigProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubClient {

    private final RestTemplate restTemplate;
    private final GithubConfigProperties githubConfigProperties;

    public GithubClient(GithubConfigProperties githubConfigProperties) {
        this.restTemplate = new RestTemplateBuilder()
            .messageConverters(new MappingJackson2HttpMessageConverter())
            .build();
        this.githubConfigProperties = githubConfigProperties;
    }

    public GithubAccessTokenResponse requestGithubToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
            code,
            githubConfigProperties.getClientId(),
            githubConfigProperties.getClientSecret()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<GithubAccessTokenRequest> httpEntity = new HttpEntity<>(githubAccessTokenRequest, headers);

        try {
            return restTemplate
                .exchange(githubConfigProperties.getAccessTokenUrl(), HttpMethod.POST, httpEntity,
                    GithubAccessTokenResponse.class)
                .getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("requestGithubToken is fail", e);
        }


    }

    public GithubProfileResponse requestGithubProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity httpEntity = new HttpEntity<>(headers);
        try {
            return restTemplate
                .exchange(githubConfigProperties.getProfileUrl(), HttpMethod.GET, httpEntity,
                    GithubProfileResponse.class)
                .getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("requestGithubProfile is fail", e);
        }
    }
}
