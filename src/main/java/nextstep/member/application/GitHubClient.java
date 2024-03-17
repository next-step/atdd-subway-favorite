package nextstep.member.application;

import nextstep.member.GitHubClientProperties;
import nextstep.member.application.dto.GitHubAccessTokenRequest;
import nextstep.member.application.dto.GitHubAccessTokenResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
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
}
