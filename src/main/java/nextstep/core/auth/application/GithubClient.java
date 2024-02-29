package nextstep.core.auth.application;

import nextstep.common.util.WebClientUtil;
import nextstep.core.auth.application.dto.GithubAccessTokenRequest;
import nextstep.core.auth.application.dto.GithubAccessTokenResponse;
import nextstep.core.auth.application.dto.GithubProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class GithubClient {

    @Value("${github.base-url}")
    private String baseUrl;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    WebClientUtil webClientUtil;

    public GithubClient(WebClientUtil webClientUtil) {
        this.webClientUtil = webClientUtil;
    }

    public GithubProfileResponse requestGithubProfile(String code) {
        return requestGithubUserInfo(requestGithubToken(code));
    }

    private GithubProfileResponse requestGithubUserInfo(String accessToken) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);

        return webClientUtil
                .get(baseUrl + "user", headers, GithubProfileResponse.class)
                .block();
    }

    private String requestGithubToken(String code) {
        return webClientUtil
                .post(baseUrl + "login/oauth/access_token",
                        createAccessTokenRequest(code),
                        MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE),
                        GithubAccessTokenResponse.class)
                .block()
                .getAccessToken();
    }

    private GithubAccessTokenRequest createAccessTokenRequest(String code) {
        return new GithubAccessTokenRequest(
                code,
                clientId,
                clientSecret
        );
    }
}
