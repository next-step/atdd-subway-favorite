package nextstep.member.tobe.application;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.tobe.AuthenticationException;
import nextstep.member.tobe.application.dto.TokenRequest;
import nextstep.member.tobe.application.dto.TokenResponse;
import nextstep.member.tobe.domain.AuthService;

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
