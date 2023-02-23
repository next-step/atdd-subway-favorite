package nextstep.member.application;

import java.util.List;
import java.util.Optional;
import nextstep.member.application.dto.CodeRequest;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.auth.Auth2Client;
import nextstep.member.auth.OAuth2User;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final Auth2Client auth2Client;

    public AuthService(
        MemberService memberService,
        JwtTokenProvider jwtTokenProvider,
        Auth2Client auth2Client
    ) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.auth2Client = auth2Client;
    }

    public TokenResponse login(TokenRequest request) {
        final Member member = memberService.findMemberByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("email 또는 password 를 확인해 주세요."));
        if (!member.checkPassword(request.getPassword())) {
            throw new IllegalArgumentException("email 또는 password 를 확인해 주세요.");
        }

        return generateToken(member.getEmail(), member.getRoles());
    }

    public TokenResponse githubLogin(CodeRequest request) {
        final String authAccessToken = auth2Client.getAccessToken(request.getCode());
        final OAuth2User oAuth2User = auth2Client.loadUser(authAccessToken);

        final Optional<Member> member = memberService.findMemberByEmail(oAuth2User.getName());

        if (member.isEmpty()) {
            final MemberResponse memberResponse = memberService.createMember(new MemberRequest(oAuth2User.getName()));
            return generateToken(memberResponse.getEmail(), memberResponse.getRoles());
        }
        return generateToken(member.get().getEmail(), member.get().getRoles());
    }

    private TokenResponse generateToken(String email, List<String> roles) {
        final String token = jwtTokenProvider.createToken(email, roles);
        return new TokenResponse(token);
    }
}
