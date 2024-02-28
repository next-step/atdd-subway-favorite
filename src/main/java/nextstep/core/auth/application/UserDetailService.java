package nextstep.core.auth.application;

import nextstep.core.auth.application.dto.GithubProfileResponse;
import nextstep.core.auth.application.dto.TokenResponse;
import nextstep.core.member.application.MemberService;
import nextstep.core.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService {

    private final TokenService tokenService;
    private final MemberService memberService;

    public UserDetailService(TokenService tokenService, MemberService memberService) {
        this.tokenService = tokenService;
        this.memberService = memberService;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.checkPassword(password)) {
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }

        return tokenService.createToken(member.getEmail());
    }

    public TokenResponse createTokenByGithub(String code) {
        GithubProfileResponse githubProfileResponse = tokenService.requestGithubProfile(code);
        Member member = memberService.findOrCreate(githubProfileResponse.getEmail());

        return tokenService.createTokenByGithub(member.getEmail());
    }
}
