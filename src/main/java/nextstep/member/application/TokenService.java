package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.global.exception.AuthenticationException;
import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createTokenFromGithub(GithubTokenRequest request) {
        var token = githubClient.requestGithubToken(request.getCode());
        var githubProfileResponse = githubClient.requestUserProfile(token);
        Member member = memberService.findMemberByEmailOrCreate(githubProfileResponse.getEmail(),
                githubProfileResponse.getAge());
        return createToken(member.getEmail(), member.getPassword());
    }
}
