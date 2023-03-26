package nextstep.member.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.RoleType;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final MemberService memberService;
    private final JwtTokenProvider tokenProvider;

    private final GithubClient githubClient;

    public TokenResponse basicLogin(TokenRequest request) {
        MemberResponse member = memberService.findMember(request.getEmail(), request.getPassword());
        String token = tokenProvider.createToken(member.getEmail(), RoleType.memberUser());
        return TokenResponse.of(token);
    }

    public GithubAccessTokenResponse githubLogin(String code) {
        String email = githubClient.getEmailFromGithub(code);

        Member member = memberService.findMemberByEmail(email)
            .orElseGet(() -> memberService.saveMember(email));

        String serverToken = tokenProvider.createToken(member.getEmail(), RoleType.memberUser());
        return new GithubAccessTokenResponse(serverToken);
    }
}
