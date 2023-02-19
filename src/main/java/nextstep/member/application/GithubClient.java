package nextstep.member.application;

import nextstep.common.exception.LoginException;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class GithubClient {
    private static final String AUTHORIZATION = "Authorization";
    private static final String ACCEPT = "Accept";
    private static final String TOKEN = "token %s";

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.url.access-token}")
    private String tokenUrl;
    @Value("${github.url.authorize-callback}")
    private String authCallbackUrl;
    @Value("${github.url.profile}")
    private String profileUrl;

    public String getAccessTokenFromGithub(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(code, clientId, clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.add(ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        var httpEntity = new HttpEntity<>(githubAccessTokenRequest, headers);

        RestTemplate restTemplate = new RestTemplate();
        GithubAccessTokenResponse githubAccessTokenResponse = restTemplate.exchange(tokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class).getBody();
        String accessToken = Objects.requireNonNull(githubAccessTokenResponse).getAccessToken();
        if (Strings.isBlank(accessToken)) {
            throw new LoginException("유효하지 않는 엑세스 토큰 입니다.");
        }
        return accessToken;
    }

    public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, String.format(TOKEN, accessToken));

        var httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            return restTemplate
                    .exchange(profileUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                    .getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException();
        }
    }
}
