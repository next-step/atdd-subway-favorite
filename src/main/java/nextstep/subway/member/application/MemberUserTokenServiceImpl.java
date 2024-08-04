package nextstep.subway.member.application;

import nextstep.subway.auth.application.JwtTokenProvider;
import nextstep.subway.auth.application.UserTokenService;
import nextstep.subway.auth.application.dto.UserTokenRequest;
import nextstep.subway.exception.AuthenticationException;
import nextstep.subway.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberUserTokenServiceImpl implements UserTokenService {
    private JwtTokenProvider jwtTokenProvider;
    private MemberService memberService;

    public MemberUserTokenServiceImpl(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public String createToken(UserTokenRequest userTokenRequest) {
        if (isInvalidRequest(userTokenRequest)) {
            return null;
        }

        Member member = memberService.findMemberByEmailOrThrow(userTokenRequest.getEmail());

        if (!member.getPassword().equals(userTokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        return jwtTokenProvider.createToken(member.getEmail());
    }

    private boolean isInvalidRequest(UserTokenRequest userTokenRequest) {
        return userTokenRequest.getEmail() == null || userTokenRequest.getEmail().isEmpty()
                || userTokenRequest.getPassword() == null || userTokenRequest.getPassword().isEmpty();
    }
}
