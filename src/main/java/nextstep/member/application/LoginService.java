package nextstep.member.application;

import nextstep.common.exception.LoginException;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

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
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }

    public TokenResponse loginGithubToken(String code) {
        String accessToken = githubClient.getAccessTokenFromGithub(code);
        return new TokenResponse(accessToken);
    }

    private Member findByEmailAndPassword(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(LoginException::new);
    }
}
