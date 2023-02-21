package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.GithubCodeRequest;
import nextstep.member.application.dto.GithubResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.exception.MemberErrorCode;
import nextstep.member.application.exception.NotFoundMemberException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.ui.GithubOauthAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    private final MemberRepository memberRepository;

    private final GithubOauthAdapter gitHubOauthAdapter;

    @Transactional(readOnly = true)
    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmailAndPassword(tokenRequest.getEmail(), tokenRequest.getPassword())
                .orElseThrow(() -> new NotFoundMemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        return new TokenResponse(jwtTokenProvider.createToken(member.getEmail(), member.getRoles()));
    }

    @Transactional
    public TokenResponse createToken(GithubCodeRequest githubCodeRequest) {
        GithubResponse githubResponse = gitHubOauthAdapter.login(githubCodeRequest.getCode());

        Member member = memberRepository.findByEmail(githubResponse.getEmail())
                .orElseGet(
                        () -> memberRepository.save(new Member(githubResponse.getEmail(), githubResponse.getCode()))
                );

        return new TokenResponse(jwtTokenProvider.createToken(member.getEmail(), member.getRoles()));
    }
}
