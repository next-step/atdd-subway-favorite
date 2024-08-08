package nextstep.member.application;

import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Transactional
    public MemberResponse createOrSaveMember(final GithubProfileResponse githubProfileResponse) {
        Optional<Member> memberOptional = memberService.findMemberOptionalByEmail(githubProfileResponse.getEmail());
        if (memberOptional.isPresent()) {
            return MemberResponse.of(memberOptional.get());
        }

        return memberService.createMember(MemberRequest.of(githubProfileResponse.getEmail(), "password", githubProfileResponse.getAge()));
    }

    public TokenResponse getAuthToken(final String code) {
        String accessToken = githubClient.requestGithubAccessToken(code);
        GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile(accessToken);
        MemberResponse memberResponse = createOrSaveMember(githubProfileResponse);
        return new TokenResponse(jwtTokenProvider.createToken(memberResponse.getEmail()));
    }
}

