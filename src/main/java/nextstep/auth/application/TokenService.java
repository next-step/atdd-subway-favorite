package nextstep.auth.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.dto.AuthMember;
import nextstep.auth.application.dto.ProfileResponse;
import nextstep.auth.application.dto.TokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenService {
    private UserDetailsService memberService;
    private TokenProvider tokenProvider;
    private ClientRequester clientRequester;

    public TokenService(UserDetailsService memberService, TokenProvider tokenProvider, ClientRequester clientRequester) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
        this.clientRequester = clientRequester;
    }

    public TokenResponse createToken(String email, String password) {
        AuthMember member = memberService.findAuthMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = tokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    @Transactional
    public TokenResponse getAuthToken(final String code) {
        String accessToken = clientRequester.requestAccessToken(code);
        ProfileResponse profileResponse = clientRequester.requestProfile(accessToken);
        AuthMember memberResponse = memberService.findAuthMemberOrOtherJob(profileResponse);
        return new TokenResponse(tokenProvider.createToken(memberResponse.getEmail()));
    }

}

