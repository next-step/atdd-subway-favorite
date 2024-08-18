package nextstep.member.domain;


import nextstep.member.domain.dto.GithubAccessTokenRequest;
import nextstep.member.domain.dto.GithubAccessTokenResponse;
import nextstep.member.domain.dto.GithubProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubClient {

    private String clientId;
    private String clientSecret;
    private String tokenRequestUri;
    private String profileUri;

    public GithubClient(
            @Value("${security.github.token.client-id}")
            String clientId,
            @Value("${security.github.token.client-secret}")
            String clientSecret,
            @Value("${security.github.token.request-uri}")
            String tokenRequestUri,
            @Value("${security.github.profile-uri}")
            String profileUri
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenRequestUri = tokenRequestUri;
        this.profileUri = profileUri;
    }

    public String getAccessToken(String code) {
        return requestToken(code);
    }

    private String requestToken(String code) {
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

        String url = tokenRequestUri; // github token request url
        return restTemplate
                .exchange(url, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                .getBody()
                .getAccessToken();
    }

    public GithubProfileResponse requestProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(accessToken, headers);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate
                .exchange(profileUri, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                .getBody();
    }
}
