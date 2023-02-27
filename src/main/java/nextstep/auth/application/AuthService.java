package nextstep.auth.application;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.MemberAuthRestApiException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, GithubClient githubClient, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
        this.memberRepository = memberRepository;
    }

    public TokenResponse creteToken(TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmail(tokenRequest.getEmail())
                                        .orElseThrow(MemberAuthRestApiException::new);

        if(!member.checkPassword(tokenRequest.getPassword())) {
            throw new MemberAuthRestApiException("유효하지 않는 토큰 정보입니다.");
        }

        return TokenResponse.of(jwtTokenProvider.createToken(member.getEmail(), member.getRoles()));
    }

    public TokenResponse createTokenByGitHub(GithubAccessTokenRequest githubAccessTokenRequest){
        String accessToken = githubClient.getAccessTokenFromGithub(githubAccessTokenRequest.getCode());
        GithubProfileResponse githubProfile = githubClient.getGithubProfileFromGithub(accessToken);

        Member member = createOrFindMember(githubProfile.getEmail());
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());

        return TokenResponse.of(token);
    }

    private Member createOrFindMember(String email) {
        Optional<Member> opMember = memberRepository.findByEmail(email);

        if (!opMember.isPresent()) {
            return memberRepository.save(new Member(email, "", 0));
        }

        return opMember.get();
    }
}
