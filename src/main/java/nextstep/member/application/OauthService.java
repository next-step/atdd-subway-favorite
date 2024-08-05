package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.AccessTokenResponse;
import nextstep.member.application.dto.ResourceResponse;
import nextstep.member.domain.OauthClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final OauthClient oauthClient;

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
        return oauthClient.requestToken(code);
    }

    private ResourceResponse getResource(String accessToken) {
        return oauthClient.requestResource(accessToken);
    }
}
