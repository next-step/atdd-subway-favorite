package nextstep.member.domain;


import nextstep.member.domain.dto.GithubAccessTokenRequest;
import nextstep.member.domain.dto.GithubAccessTokenResponse;
import nextstep.member.domain.dto.GithubProfileResponse;
import nextstep.utils.RestTemplateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

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
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<GithubAccessTokenRequest> httpEntity = new HttpEntity<>(
                githubAccessTokenRequest, headers);

        return RestTemplateUtils.post(httpEntity, tokenRequestUri, GithubAccessTokenResponse.class).getAccessToken();
    }

    public GithubProfileResponse requestProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<GithubAccessTokenRequest> httpEntity = new HttpEntity<>(headers);

        return RestTemplateUtils.getWithHeaders(httpEntity, profileUri, GithubProfileResponse.class);
    }
}
