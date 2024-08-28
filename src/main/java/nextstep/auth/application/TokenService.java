package nextstep.auth.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.domain.OAuthProvider;
import nextstep.auth.domain.OAuthUser;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;
    private GithubOAuthService githubOAuthService;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider, GithubOAuthService githubOAuthService) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubOAuthService = githubOAuthService;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원의 요청입니다."));
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createTokenByOauthLogin(OAuthProvider oAuthProvider, String code) {
        OAuthService oAuthService = null;
        switch (oAuthProvider) {
            case GITHUB:
                oAuthService = githubOAuthService;
        }

        if (oAuthService == null) {
            throw new AuthenticationException();
        }

        OAuthUser oAuthUser = oAuthService.loadUserProfile(code);

        String token = memberService.findMemberByEmail(oAuthUser.getEmail())
                .map(e -> jwtTokenProvider.createToken(e.getEmail()))
                .orElseGet(() -> {
                    MemberResponse memberResponse = memberService.registerOAuthMember(oAuthUser.getEmail());
                    return jwtTokenProvider.createToken(memberResponse.getEmail());
                });

        return new TokenResponse(token);
    }
}
