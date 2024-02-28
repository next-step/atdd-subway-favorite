package nextstep.auth.application;

import nextstep.auth.AuthenticationException;
import nextstep.member.application.MemberService;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;
    private final MemberRepository memberRepository;

    public TokenService(
            MemberService memberService,
            JwtTokenProvider jwtTokenProvider,
            GithubClient githubClient,
            MemberRepository memberRepository
    ) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
        this.memberRepository = memberRepository;
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
        String accessToken = githubClient.requestToken(code);
        GithubProfileResponse profile = githubClient.requestUser(accessToken);

        memberRepository.findByEmail(profile.getEmail())
                .orElseGet(() -> memberRepository.save(new Member(profile.getEmail(), "github-login", profile.getAge())));

        return new TokenResponse(accessToken);
    }
}
