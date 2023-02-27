package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.stub.GithubResponses;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GithubService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public GithubAccessTokenResponse getAuth(GithubAccessTokenRequest request) {
        memberRepository.findByEmail(request.getClientId())
                .orElseGet(() -> memberRepository.save(new Member(request.getClientId(), request.getClientSecret())));
        String token = GithubResponses.findTokenByCode(request.getCode());
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(token);
        return response;
    }

}
