package nextstep.auth.application;

import nextstep.auth.GitHubClientProperties;
import nextstep.auth.application.dto.GitHubAccessTokenRequest;
import nextstep.auth.application.dto.GitHubAccessTokenResponse;
import nextstep.auth.application.dto.GitHubProfileResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GitHubClient {
    private final GitHubClientProperties gitHubClientProperties;
    private final RestTemplate restTemplate;

    public GitHubClient(GitHubClientProperties gitHubClientProperties) {
        this.gitHubClientProperties = gitHubClientProperties;
        this.restTemplate = new RestTemplateBuilder()
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .rootUri(gitHubClientProperties.getBaseUrl())
                .messageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    public String requestGitHubAccessToken(String code) {
        String url = "/github/login/oauth/access_token";
        GitHubAccessTokenRequest requestBody = new GitHubAccessTokenRequest(
                code,
                gitHubClientProperties.getId(),
                gitHubClientProperties.getSecret()
        );

        return restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        new HttpEntity<>(requestBody),
                        GitHubAccessTokenResponse.class
                )
                .getBody()
                .getAccessToken();
    }

    public GitHubProfileResponse requestGithubProfile(final String accessToken) {
        String url = "/github/user";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        return restTemplate
                .exchange(
                        url,
                        HttpMethod.POST,
                        new HttpEntity<>(headers),
                        GitHubProfileResponse.class
                )
                .getBody();
    }
}
