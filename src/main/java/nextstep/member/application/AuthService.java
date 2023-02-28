package nextstep.member.application;

import nextstep.exception.member.PasswordNotEqualException;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberRequest;
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

    public TokenResponse loginGithub(String code) {
        String accessToken = githubClient.getAccessTokenFromGithub(code);
        GithubProfileResponse profile = githubClient.getGithubProfileFromGithub(accessToken);
        if (isNotExistMemberEmail(profile.getEmail())) {
            memberService.createMember(MemberRequest.from(profile));
        }

        return new TokenResponse(accessToken);
    }

    private boolean isNotExistMemberEmail(String email) {
        return !memberService.isExistMemberByEmail(email);
    }
}
