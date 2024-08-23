package nextstep.member.application;

import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.exception.MemberNotFoundException;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

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

        String token = jwtTokenProvider.createToken(member.getEmail(), member.getId());

        return new TokenResponse(token);
    }

    public TokenResponse createToken(String code) {
        String githubToken = githubClient.requestGithubToken(code);
        GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile(githubToken);

        Member member = lookUpOrCreateMember(githubProfileResponse);

        String token = jwtTokenProvider.createToken(member.getEmail(), member.getId());

        return new TokenResponse(token);
    }

    private Member lookUpOrCreateMember(GithubProfileResponse githubProfileResponse) {
        Member member;
        try {
            member = memberService.findMemberByEmail(githubProfileResponse.getEmail());
        } catch (MemberNotFoundException exception) {
            member = memberService.save(MemberRequest.of(githubProfileResponse.getEmail(), githubProfileResponse.getAge()));
        }
        return member;
    }
}
