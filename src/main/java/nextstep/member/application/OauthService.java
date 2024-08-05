package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.AccessTokenResponse;
import nextstep.member.application.dto.ClientInfo;
import nextstep.member.application.dto.ResourceResponse;
import nextstep.member.domain.OauthTokenClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    @Value("${github.authorization.token.url}")
    private String accessTokenUrl;

    @Value("${github.authorization.client.id}")
    private String clientId;

    @Value("${github.authorization.clientSecret}")
    private String clientSecret;

    @Value("${github.authorization.resource.url}")
    private String resourceUrl;

    private final OauthTokenClient oauthTokenClient;

    public ResourceResponse authenticate(String code) {
        AccessTokenResponse accessToken = getAccessToken(code);
        if (accessToken.getAccessToken().isBlank()) {
            throw new AuthenticationException();
        }

        ResourceResponse resource = getResource(accessToken.getAccessToken());
        if (resource.getEmail().isBlank()) {
            throw new AuthenticationException();
        }

        return resource;
    }

    private AccessTokenResponse getAccessToken(String code) {
        ClientInfo clientInfo = new ClientInfo(accessTokenUrl, clientId, clientSecret);
        return oauthTokenClient.requestToken(clientInfo, code);
    }

    private ResourceResponse getResource(String accessToken) {
        ClientInfo clientInfo = new ClientInfo(resourceUrl, clientId, clientSecret);
        return oauthTokenClient.requestResource(clientInfo, "bearer " + accessToken);
    }
}
