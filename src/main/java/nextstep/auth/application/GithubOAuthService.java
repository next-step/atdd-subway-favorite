package nextstep.auth.application;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.domain.GithubOAuthUser;
import nextstep.auth.domain.OAuthUser;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubOAuthService implements OAuthService {

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.url.access-token}")
    private String requestTokenUrl;

    @Value("${github.url.profile}")
    private String requestProfileUrl;

    @Override
    public OAuthUser loadUserProfile(String code) {
        GithubProfileResponse githubProfileResponse = requestUserProfile(requestAccessToken(code));

        return new GithubOAuthUser(githubProfileResponse.getEmail());
    }

    public String requestAccessToken(String code) {
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

        String url = requestTokenUrl;
        return restTemplate
                .exchange(url, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                .getBody().getAccessToken();
    }

    public GithubProfileResponse requestUserProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        RestTemplate restTemplate = new RestTemplate();

        String url = requestProfileUrl;
        return restTemplate
                .exchange(url, HttpMethod.GET, new HttpEntity<>(headers), GithubProfileResponse.class)
                .getBody();
    }
}
