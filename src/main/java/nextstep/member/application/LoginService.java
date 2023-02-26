package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.message.Message;
import nextstep.member.domain.GithubMember;
import nextstep.member.domain.GithubMemberRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;
    private final GithubMemberRepository githubMemberRepository;

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmail(tokenRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(Message.NOT_EXIST_EAMIL));
        if (!member.arePasswordsSame(tokenRequest.getPassword())) {
            throw new IllegalArgumentException(Message.INVALID_PASSWORD);
        }
        return new TokenResponse(jwtTokenProvider.createToken(member.getEmail(), member.getRoles()));
    }

    public GithubAccessTokenResponse getGithubToken(String code) {
        return new GithubAccessTokenResponse(githubClient.getAccessTokenFromGithub(code));
    }

    public GithubAccessTokenResponse getAuth(GithubAccessTokenRequest request) {
        Optional<GithubMember> maybeGithubMember = githubMemberRepository.findByCode(request.getCode());
        if (maybeGithubMember.isPresent()) {
            GithubMember githubMember = maybeGithubMember.get();
            GithubAccessTokenResponse response = new GithubAccessTokenResponse(githubMember.getAccessToken());
            return response;
        }
        GithubMember githubMember = githubMemberRepository.save(request.getCode(), request.getClientSecret(), request.getClientId());
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(githubMember.getAccessToken());
        return response;
    }
}
