package nextstep.auth.application.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.application.exception.AuthenticationException;
import nextstep.auth.oauth.github.GithubAccessTokenResponse;
import nextstep.auth.oauth.github.GithubClient;
import nextstep.auth.oauth.github.GithubProfileResponse;
import nextstep.common.error.exception.NotFoundException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TokenService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;


    public TokenResponse createToken(String email, String password) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isEmpty()) {
            throw new NotFoundException();
        }
        Member member = memberOptional.get();
        if (!member.checkPassword(password)) {
            throw new AuthenticationException();
        }
        String token = jwtTokenProvider.createToken(member.getEmail());
        return new TokenResponse(token);
    }


    @Transactional
    public TokenResponse createTokenByGithub(String code) {
        GithubAccessTokenResponse githubAccessTokenResponse = githubClient.requestGithubToken(code);
        GithubProfileResponse githubProfileResponse = githubClient
            .requestGithubProfile(githubAccessTokenResponse.getAccessToken());

        Member member = memberRepository.findByEmail(githubProfileResponse.getEmail())
            .orElseGet(() -> memberRepository.save(
                new Member(githubProfileResponse.getEmail(), "default password", null))
            );
        return createToken(member.getEmail(), member.getPassword());
    }
}
