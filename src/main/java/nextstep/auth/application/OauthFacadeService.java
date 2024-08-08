package nextstep.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.dto.ApplicationTokenResponse;
import nextstep.auth.application.dto.ResourceResponse;
import nextstep.auth.domain.UserDetails;
import nextstep.auth.domain.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthFacadeService {

    private final OauthClientService oauthClientService;
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    public ApplicationTokenResponse getApplicationToken(String code) {
        ResourceResponse resourceResponse = oauthClientService.authenticate(code);
        UserDetails userDetails = userDetailsService.loadByUserEmail(resourceResponse.getEmail(), resourceResponse.getAge());
        return tokenService.createToken(userDetails.getEmail(), userDetails.getPassword());
    }
}
