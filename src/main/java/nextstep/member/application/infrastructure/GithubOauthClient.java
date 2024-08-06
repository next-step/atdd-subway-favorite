package nextstep.member.application.infrastructure;

import lombok.extern.slf4j.Slf4j;
import nextstep.member.AccessTokenException;
import nextstep.member.MemberErrorMessage;
import nextstep.member.application.dto.ResourceResponse;
import nextstep.member.application.dto.ApplicationTokenResponse;
import nextstep.member.application.dto.github.GithubAccessTokenRequest;
import nextstep.member.application.dto.github.GithubAccessTokenResponse;
import nextstep.member.application.dto.github.GithubProfileResponse;
import nextstep.member.domain.OauthClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class GithubOauthClient implements OauthClient {

    @Value("${github.authorization.token.url}")
    private String accessTokenUrl;

    @Value("${github.authorization.client.id}")
    private String clientId;

    @Value("${github.authorization.clientSecret}")
    private String clientSecret;

    @Value("${github.authorization.resource.url}")
    private String resourceUrl;

    @Override
    public ApplicationTokenResponse requestToken(String code) {
        GithubAccessTokenRequest requestBody = new GithubAccessTokenRequest(
                code,
                clientId,
                clientSecret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
                requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();


        try {
            GithubAccessTokenResponse githubAccessTokenResponse = restTemplate
                    .exchange(accessTokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                    .getBody();
            return new ApplicationTokenResponse(githubAccessTokenResponse.getAccessToken());
        } catch (NullPointerException exception) {
            log.error("request accessToken error ", exception);
            throw new AccessTokenException(MemberErrorMessage.NOT_VALID_USER_CODE);
        }
    }

    @Override
    public ResourceResponse requestResource(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
                null, headers);
        RestTemplate restTemplate = new RestTemplate();

        GithubProfileResponse githubProfileResponse = restTemplate
                .exchange(resourceUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                .getBody();

        try {
            return new ResourceResponse(githubProfileResponse.getEmail(), githubProfileResponse.getAge());
        } catch (NullPointerException exception) {
            log.error("request accessToken error ", exception);
            throw new AccessTokenException(MemberErrorMessage.NOT_VALID_USER_CODE);
        }
    }
}
