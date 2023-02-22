package nextstep.member.application;

import nextstep.member.domain.Member;
import nextstep.member.infrastructure.GithubClientImpl;
import nextstep.member.infrastructure.dto.GithubTokenRequest;
import nextstep.member.infrastructure.dto.MemberInfo;
import nextstep.member.ui.request.TokenRequest;
import nextstep.member.ui.response.TokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final GithubClientImpl githubClientImpl;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberService memberService, GithubClientImpl githubClientImpl) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
        this.githubClientImpl = githubClientImpl;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = memberService.findMemberByEmail(request.getEmail());
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return TokenResponse.of(token);
    }

    public MemberInfo findMemberByToken(String accessToken) {
        String email = jwtTokenProvider.getPrincipal(accessToken);
        return MemberInfo.from(memberService.findMemberByEmail(email));
    }

    public TokenResponse getGithubToken(GithubTokenRequest request) {
        Optional<String> token = githubClientImpl.getAccessToken(request.getCode());
        return token.map(TokenResponse::of).orElseGet(() -> joinGithubMember(request));
    }

    private TokenResponse joinGithubMember(GithubTokenRequest request) {

    }
}
