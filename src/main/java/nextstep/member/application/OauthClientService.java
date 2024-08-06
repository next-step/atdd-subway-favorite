package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.ResourceResponse;
import nextstep.member.application.dto.ApplicationTokenResponse;
import nextstep.member.domain.OauthClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthClientService {

    private final OauthClient oauthClient;

    public ResourceResponse authenticate(String code) {
        ApplicationTokenResponse accessToken = getAccessToken(code);
        if (accessToken.getAccessToken().isBlank()) {
            throw new AuthenticationException();
        }

        ResourceResponse resource = getResource(accessToken.getAccessToken());
        if (resource.getEmail().isBlank()) {
            throw new AuthenticationException();
        }

        return resource;
    }

    private ApplicationTokenResponse getAccessToken(String code) {
        return oauthClient.requestToken(code);
    }

    private ResourceResponse getResource(String accessToken) {
        return oauthClient.requestResource(accessToken);
    }
}
