package nextstep.member.application.infrastructure;

import lombok.extern.slf4j.Slf4j;
import nextstep.member.AccessTokenException;
import nextstep.member.MemberErrorMessage;
import nextstep.member.application.dto.AccessTokenResponse;
import nextstep.member.application.dto.ClientInfo;
import nextstep.member.application.dto.ResourceResponse;
import nextstep.member.application.dto.github.GithubAccessTokenRequest;
import nextstep.member.application.dto.github.GithubAccessTokenResponse;
import nextstep.member.application.dto.github.GithubProfileResponse;
import nextstep.member.domain.OauthTokenClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class GithubOauthTokenClient implements OauthTokenClient {

    @Override
    public AccessTokenResponse requestToken(ClientInfo clientInfo, String code) {
        GithubAccessTokenRequest requestBody = new GithubAccessTokenRequest(
                code,
                clientInfo.getClientId(),
                clientInfo.getClientSecret()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
                requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();


        try {
            GithubAccessTokenResponse githubAccessTokenResponse = restTemplate
                    .exchange(clientInfo.getUrl(), HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                    .getBody();
            return new AccessTokenResponse(githubAccessTokenResponse.getAccessToken());
        } catch (NullPointerException exception) {
            log.error("request accessToken error ", exception);
            throw new AccessTokenException(MemberErrorMessage.NOT_VALID_USER_CODE);
        }
    }

    @Override
    public ResourceResponse requestResource(ClientInfo clientInfo, String accessToken) {
//        GithubResourceRequest githubResourceRequest = new GithubResourceRequest(accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
                null, headers);
        RestTemplate restTemplate = new RestTemplate();

        GithubProfileResponse githubProfileResponse = restTemplate
                .exchange(clientInfo.getUrl(), HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                .getBody();

        try {
            return new ResourceResponse(githubProfileResponse.getEmail());
        } catch (NullPointerException exception) {
            log.error("request accessToken error ", exception);
            throw new AccessTokenException(MemberErrorMessage.NOT_VALID_USER_CODE);
        }
    }
}
