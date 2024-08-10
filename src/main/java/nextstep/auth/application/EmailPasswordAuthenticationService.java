package nextstep.auth.application;

import lombok.AllArgsConstructor;
import nextstep.auth.infrastructure.token.TokenGenerator;
import nextstep.auth.application.dto.EmailPasswordAuthRequest;
import nextstep.auth.dto.TokenResponse;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.domain.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailPasswordAuthenticationService {

    private final UserDetailsService userDetailsService;
    private final TokenGenerator tokenGenerator;

    public TokenResponse authenticate(EmailPasswordAuthRequest request) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        if (!userDetails.getPassword().equals(request.getPassword())) {
            throw new AuthenticationException();
        }

        String token = tokenGenerator.createToken(userDetails.getUsername());
        return new TokenResponse(token);
    }
}
