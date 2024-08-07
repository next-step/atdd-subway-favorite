package nextstep.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.application.dto.ResourceResponse;
import nextstep.auth.application.dto.ApplicationTokenResponse;
import nextstep.auth.domain.OauthClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthClientService {

    private final OauthClient oauthClient;

    public ResourceResponse authenticate(String code) {
        ApplicationTokenResponse accessToken = getAccessToken(code);
        if (inValid(accessToken.getAccessToken())) {
            throw new AuthenticationException();
        }

        ResourceResponse resource = getResource(accessToken.getAccessToken());
        if (inValid(resource.getEmail())) {
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

    private boolean inValid(String source) {
        return source == null || source.isBlank();
    }
}
