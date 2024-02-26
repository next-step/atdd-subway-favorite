package nextstep.auth.application;

import nextstep.fake.GithubAccessTokenRequest;
import nextstep.fake.GithubAccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Component
public class GithubClient {

    @Value("${github.url}")
    private String githubUrl;

    public String requestGithubToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        GithubAccessTokenRequest request = new GithubAccessTokenRequest(
                code,
                "client_id",
                "client_secret"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity httpEntity = new HttpEntity(request, headers);
        String accessToken = restTemplate.exchange(githubUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                .getBody()
                .getAccessToken();

        return accessToken;
    }



}
