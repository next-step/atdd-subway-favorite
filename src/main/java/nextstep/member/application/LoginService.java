package nextstep.member.application;

import nextstep.common.exception.LoginException;
import nextstep.common.utils.JwtTokenProvider;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public LoginService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse loginToken(TokenRequest tokenRequest) {
        Member member = findByEmailAndPassword(tokenRequest.getEmail(), tokenRequest.getPassword());
        return createToken(member.getEmail(), member.getRoles());
    }

    public TokenResponse loginGithubToken(String code) {
        String githubAccessToken = githubClient.getAccessTokenFromGithub(code);
        return createToken(githubAccessToken, List.of(RoleType.ROLE_MEMBER.name()));
    }

    private TokenResponse createToken(String principal, List<String> roles) {
        String token = jwtTokenProvider.createToken(principal, roles);
        return new TokenResponse(token);
    }

    private Member findByEmailAndPassword(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(LoginException::new);
    }
}
