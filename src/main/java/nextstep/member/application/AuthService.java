package nextstep.member.application;

import nextstep.exception.MemberInvalidException;
import nextstep.exception.MemberNotFoundException;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.LoginRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse login(final TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmail(tokenRequest.getEmail())
                .orElseThrow(MemberNotFoundException::new);
        if (checkInvalidMember(tokenRequest, member)) {
            throw new MemberInvalidException();
        }
        return new TokenResponse(jwtTokenProvider.createToken(member.getEmail(), member.getRoles()));
    }

    @Transactional
    public TokenResponse loginByGithub(final LoginRequest loginRequest) {
        GithubProfileResponse profile = githubClient.callLoginApi(loginRequest);

        Member member = memberRepository.findByEmail(profile.getEmail())
                .orElse(memberRepository.save(new Member(profile.getEmail(), "password", 20)));
        return new TokenResponse(jwtTokenProvider.createToken(member.getEmail(), member.getRoles()));
    }

    private static boolean checkInvalidMember(TokenRequest tokenRequest, Member member) {
        return !member.checkPassword(tokenRequest.getPassword());
    }
}
