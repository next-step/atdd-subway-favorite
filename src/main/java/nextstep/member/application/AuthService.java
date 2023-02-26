package nextstep.member.application;

import nextstep.member.domain.Member;
import nextstep.member.infrastructure.SocialClient;
import nextstep.member.infrastructure.dto.GithubTokenRequest;
import nextstep.member.infrastructure.dto.LoginMember;
import nextstep.member.infrastructure.dto.ProfileDto;
import nextstep.member.ui.request.TokenRequest;
import nextstep.member.ui.response.TokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final SocialClient socialClient;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberService memberService, SocialClient socialClient) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
        this.socialClient = socialClient;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = memberService.findMemberByEmail(request.getEmail());
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return TokenResponse.of(token);
    }

    public LoginMember findMemberByToken(String accessToken) {
        String email = jwtTokenProvider.getPrincipal(accessToken);
        return LoginMember.from(memberService.findMemberByEmail(email));
    }

    public TokenResponse login(GithubTokenRequest request) {
        ProfileDto profileDto = socialClient.getProfileFromGithub(request.getCode());
        Member member = memberService.getJoinedMember(profileDto.getEmail());
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return TokenResponse.of(token);
    }
}
