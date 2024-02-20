package nextstep.member.application;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@ConfigurationProperties
public class GithubClient {

    @Value("${github.client.url}")
    private String githubUrl;

    @Value("${github.client.id}")
    private String githubClientId;

    @Value("${github.client.secret}")
    private String githubClientSecret;




    public String requestGithubToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
            code,
            githubClientId,
            githubClientSecret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
            githubAccessTokenRequest, headers);
        RestTemplate restTemplate = new RestTemplate();

        final String url = githubUrl + "/github/login/oauth/access_token"; // github token request url
        String accessToken = restTemplate
            .exchange(url, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
            .getBody()
            .getAccessToken();

        return accessToken;
    }

    public GithubProfileResponse requestGithubProfile(String accessToken) {
HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "bearer " + accessToken);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity httpEntity = new HttpEntity(headers);

        RestTemplate restTemplate = new RestTemplate();
        final String url = githubUrl + "/github/user"; // github profile request url

        return restTemplate
            .exchange(url, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
            .getBody();
    }
}
