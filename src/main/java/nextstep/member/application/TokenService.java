package nextstep.member.application;

import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.ProfileResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TokenService {
    private UserDetailsService memberService;
    private TokenProvider tokenProvider;
    private ClientRequester clientRequester;

    public TokenService(UserDetailsService memberService, TokenProvider tokenProvider, ClientRequester clientRequester) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
        this.clientRequester = clientRequester;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = tokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    @Transactional
    public TokenResponse getAuthToken(final String code) {
        String accessToken = clientRequester.requestGithubAccessToken(code);
        ProfileResponse profileResponse = clientRequester.requestGithubProfile(accessToken);
        MemberResponse memberResponse = findOrCreateMember(profileResponse);
        return new TokenResponse(tokenProvider.createToken(memberResponse.getEmail()));
    }

    public MemberResponse findOrCreateMember(ProfileResponse profileResponse) {
        Optional<Member> memberOptional = memberService.findMemberOptionalByEmail(profileResponse.getEmail());
        if (memberOptional.isPresent()) {
            return MemberResponse.of(memberOptional.get());
        }

        return createMember(profileResponse);
    }

    private MemberResponse createMember(ProfileResponse profileResponse) {
        MemberRequest memberRequest = MemberRequest.of(
                profileResponse.getEmail(),
                "password",
                profileResponse.getAge()
        );
        return memberService.createMember(memberRequest);
    }
}

