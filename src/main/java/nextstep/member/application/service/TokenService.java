package nextstep.member.application.service;

import nextstep.exception.AuthenticationException;
import nextstep.auth.application.GithubClient;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.entity.Member;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class TokenService {
    private MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createGithubToken(String code) {
        String githubToken = githubClient.requestGithubToken(code);
        String email = githubClient.requestGithubProfile(githubToken).getEmail();

        try {
            memberService.findMemberByEmail(email);
        } catch (EntityNotFoundException e) {
            memberService.createMember(new MemberRequest(email, "", 0));
        }

        return new TokenResponse(jwtTokenProvider.createToken(email));
    }
}
