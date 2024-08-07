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
        if (!isValid(accessToken.getAccessToken())) {
            throw new AuthenticationException();
        }

        ResourceResponse resource = getResource(accessToken.getAccessToken());
        if (!isValid(resource.getEmail())) {
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

    private boolean isValid(String source) {
        return !(source == null || source.isBlank());
    }
}
