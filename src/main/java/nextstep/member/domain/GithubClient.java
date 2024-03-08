package nextstep.member.domain;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class GithubClient {
    public String requestGithubToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
            code,
            "clientId", // client id
            "clientSecret" // client secret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
            githubAccessTokenRequest, headers);
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/github/login/oauth/access_token"; // github token request url
        String accessToken = restTemplate
            .exchange(url, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
            .getBody()
            .getAccessToken();

        return accessToken;
    }

}
