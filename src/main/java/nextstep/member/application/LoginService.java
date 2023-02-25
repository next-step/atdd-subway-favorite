package nextstep.member.application;

import nextstep.member.application.dto.CodeRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.auth.OAuth2Client;
import nextstep.member.auth.OAuth2User;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2Client oAuth2Client;

    public LoginService(
        MemberService memberService,
        JwtTokenProvider jwtTokenProvider,
        OAuth2Client oAuth2Client
    ) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.oAuth2Client = oAuth2Client;
    }

    public TokenResponse login(TokenRequest request) {
        final Member member = memberService.findMemberByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("email 또는 password 를 확인해 주세요."));
        if (!member.checkPassword(request.getPassword())) {
            throw new IllegalArgumentException("email 또는 password 를 확인해 주세요.");
        }

        return generateToken(member);
    }

    public TokenResponse githubLogin(CodeRequest request) {
        final String authAccessToken = oAuth2Client.getAccessToken(request.getCode());
        final OAuth2User oAuth2User = oAuth2Client.loadUser(authAccessToken);

        final Member member = memberService.findMemberByEmail(oAuth2User.getName())
            .orElseGet(() -> memberService.createMember(oAuth2User.getName()));

        return generateToken(member);
    }

    private TokenResponse generateToken(Member member) {
        final String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }
}
