package nextstep.core.member.application;

import nextstep.core.member.application.dto.GithubAccessTokenRequest;
import nextstep.core.member.application.dto.GithubAccessTokenResponse;
import nextstep.core.member.application.dto.GithubProfileRequest;
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

    @Value("${github.base-url}")
    private String baseUrl;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    public GithubProfileResponse requestMemberInfo(String code) {
        // 1. 코드로 토큰 요청하기
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
                code,
                clientId,
                clientSecret
        );

        HttpHeaders accessTokenRequestHeaders = new HttpHeaders();
        accessTokenRequestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> accessTokenRequestHttpEntity = new HttpEntity(
                githubAccessTokenRequest, accessTokenRequestHeaders);

        RestTemplate restTemplate = new RestTemplate();

        String accessTokenRequestUrl = baseUrl + "login/oauth/access_token"; // github token request url

        // 2. 토큰 받기
        String accessToken = restTemplate
                .exchange(accessTokenRequestUrl, HttpMethod.POST, accessTokenRequestHttpEntity, GithubAccessTokenResponse.class)
                .getBody()
                .getAccessToken();

        RestTemplate restTemplate2 = new RestTemplate();

        // 3. 토큰으로 사용자 정보 요청하기
        HttpHeaders profileRequestHeaders = new HttpHeaders();
        profileRequestHeaders.add(HttpHeaders.AUTHORIZATION, accessToken);

        HttpEntity<MultiValueMap<String, String>> profileRequestHttpEntity = new HttpEntity(profileRequestHeaders);

        String profilerRequestUrl = baseUrl + "user"; // github token request url

        // 2. 토큰 받기
        return restTemplate2
                .exchange(profilerRequestUrl, HttpMethod.GET, profileRequestHttpEntity, GithubProfileResponse.class)
                .getBody();
    }
}
