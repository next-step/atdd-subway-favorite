package nextstep.member.application;

import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;
    private MemberRepository memberRepository;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
        this.memberRepository = memberRepository;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse githubLogin(String code) {
        String accessToken = githubClient.requestGithubToken(code);
        GithubProfileResponse profile = githubClient.requestGithubProfile(accessToken);

        Member member = memberRepository.findByEmail(profile.getEmail()).orElse(null);
        if(member == null) {
            memberRepository.save(new Member(profile.getEmail(), "", profile.getAge()));
        }

        String token = jwtTokenProvider.createToken(profile.getEmail());
        return new TokenResponse(token);
    }
}
