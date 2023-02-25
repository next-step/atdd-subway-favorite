package nextstep.auth.application;

import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.domain.GithubLoginRequest;
import nextstep.auth.domain.Oauth2Client;
import nextstep.auth.domain.ProfileResponse;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.common.constants.ErrorConstant.INVALID_EMAIL_PASSWORD;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final Oauth2Client client;

    public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider, Oauth2Client client) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.client = client;
    }

    public TokenResponse login(final TokenRequest tokenRequest) {
        final Member member = memberService.findByEmail(tokenRequest.getEmail());
        if (!member.checkPassword(tokenRequest.getPassword())) {
            throw new IllegalArgumentException(INVALID_EMAIL_PASSWORD);
        }

        return createToken(member);
    }

    public TokenResponse oauth2Login(final GithubLoginRequest loginRequest) {
        final String accessToken = client.getAccessToken(loginRequest.getCode());
        final ProfileResponse profile = client.getProfile(accessToken);

        final Member member = memberService.findByEmail(profile.getEmail());
        return createToken(member);
    }

    private TokenResponse createToken(final Member member) {
        final String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }
}
