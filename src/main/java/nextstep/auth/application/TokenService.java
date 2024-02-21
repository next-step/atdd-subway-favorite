package nextstep.auth.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.domain.UserDetail;
import nextstep.member.application.MemberService;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private UserDetailService userDetailService;
    private JwtTokenProvider jwtTokenProvider;

    public TokenService(UserDetailService userDetailService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailService = userDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(String email, String password) {
        UserDetail userDetail = userDetailService.findByEmail(email);
        if (!userDetail.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(userDetail.getEmail());

        return new TokenResponse(token);
    }

}
