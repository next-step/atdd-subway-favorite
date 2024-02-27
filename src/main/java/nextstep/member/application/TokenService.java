package nextstep.member.application;

import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {
    private MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private GithubClient githubClient;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse doGithubLogin(String code) {
        String token = githubClient.requestGithubToken(code);
        GithubProfileResponse response = githubClient.requestUserProfile(token);
        Optional<Member> member = memberService.findMemberByEmailForGithub(response.getEmail());

        if (member.isEmpty()){
            memberService.createMember(new MemberRequest(response.getEmail(), null, response.getAge()));
            String accessToken = jwtTokenProvider.createToken(response.getEmail());
            return new TokenResponse(accessToken);
        }

        String accessToken = jwtTokenProvider.createToken(member.get().getEmail());
        return new TokenResponse(accessToken);
    }
}
