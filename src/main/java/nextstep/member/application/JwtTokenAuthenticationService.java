package nextstep.member.application;

import nextstep.member.domain.LoginMember;
import nextstep.security.domain.Authentication;
import nextstep.member.application.dto.TokenResponse;
import nextstep.security.domain.UserInfo;
import nextstep.security.application.JwtTokenProvider;
import nextstep.security.application.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class JwtTokenAuthenticationService {
    private final UserDetailsService<Long, String> userDetailsService;
    private final JwtTokenProvider<LoginMember> jwtTokenProvider;

    public JwtTokenAuthenticationService(final UserDetailsService<Long, String> userDetailsService, final JwtTokenProvider<LoginMember> jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(UserInfo userInfo) {
        Authentication<Long, String> authentication = userDetailsService.authenticateByUserInfo(userInfo);
        String token = jwtTokenProvider.createToken(new LoginMember(authentication.getCredentials(), authentication.getPrincipal()));
        return new TokenResponse(token);
    }

}
