package nextstep.auth.application;

import nextstep.auth.application.dto.ApplicationTokenResponse;
import nextstep.auth.domain.UserDetails;
import nextstep.auth.domain.UserDetailsService;
import nextstep.auth.exception.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public TokenService(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    public ApplicationTokenResponse createToken(String email, String password) {
        UserDetails userDetails = userDetailsService.loadByUserEmail(email);
        if (!userDetails.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(userDetails.getEmail());
        return new ApplicationTokenResponse(token);
    }
}
