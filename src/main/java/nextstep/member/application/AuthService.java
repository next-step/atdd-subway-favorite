package nextstep.member.application;

import nextstep.exception.member.PasswordNotEqualException;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.utils.ObjectStringMapper;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse loginMember(TokenRequest tokenRequest) {
        Member member = memberService.findMemberByEmail(tokenRequest.getEmail());
        if (!member.checkPassword(tokenRequest.getPassword())) {
            throw new PasswordNotEqualException();
        }
        String token = jwtTokenProvider.createToken(ObjectStringMapper.convertObjectAsString(MemberResponse.of(member)), member.getRoles());
        return new TokenResponse(token);
    }

    public String loginGithub(String code) {
        return githubClient.getAccessTokenFromGithub(code);
    }
}
