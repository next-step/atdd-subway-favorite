package nextstep.member.application;

import org.springframework.stereotype.Service;

import nextstep.member.authentication.github.GithubClient;
import nextstep.member.authentication.github.dto.GithubProfileResponse;
import nextstep.member.domain.Member;
import nextstep.member.exception.InvalidPasswordException;

@Service
public class LoginService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public LoginService(MemberService memberService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public String createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.checkPassword(password)) {
            throw new InvalidPasswordException();
        }

        return jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
    }

    public String createGithubToken(String code) {
        String accessToken = githubClient.getAccessTokenFromGithub(code);
        GithubProfileResponse profile = githubClient.getGithubProfileFromGithub(accessToken);
        Member member = memberService.findOrCreateMember(profile.getEmail());
        return jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
    }
}
