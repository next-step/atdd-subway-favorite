package nextstep.member.application;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.AuthService;
import nextstep.member.domain.UserDetails;
import nextstep.member.domain.UserDetailsService;

@Service
@Qualifier("emailPasswordAuthService")
public class EmailPasswordAuthService implements AuthService {
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public EmailPasswordAuthService(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public TokenResponse login(TokenRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        if (!userDetails.getPassword().equals(request.getPassword())) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(userDetails.getUsername());
        return TokenResponse.of(token);
    }
}
