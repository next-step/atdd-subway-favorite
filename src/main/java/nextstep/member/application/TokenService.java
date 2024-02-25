package nextstep.member.application;

import nextstep.member.AuthenticationException;
import nextstep.member.application.request.GetAccessTokenRequest;
import nextstep.member.application.response.GetAccessTokenResponse;
import nextstep.member.application.response.TokenResponse;
import nextstep.member.application.response.github.GithubProfileResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenService {

    private MemberService memberService;
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;

    public TokenService(MemberService memberService, MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
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
    public GetAccessTokenResponse getAccessToken(GetAccessTokenRequest getAccessTokenRequest) {
        String githubAccessCode = githubClient.requestGithubToken(getAccessTokenRequest.getCode());
        GithubProfileResponse githubProfileResponse = githubClient.requestGithubResource(githubAccessCode);

        Member member = memberRepository.findByEmail(githubProfileResponse.getEmail())
                .orElseGet(() -> Member.of(githubProfileResponse.getEmail(), "", null));
        memberRepository.save(member);

        String token = jwtTokenProvider.createToken(member.getEmail());

        return GetAccessTokenResponse.from(token);
    }

}
