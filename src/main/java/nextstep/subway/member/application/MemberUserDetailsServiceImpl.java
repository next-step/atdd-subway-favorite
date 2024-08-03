package nextstep.subway.member.application;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.application.dto.UserDetailsRequest;
import nextstep.subway.exception.AuthenticationException;
import nextstep.subway.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberUserDetailsServiceImpl implements UserDetailsService {
    private MemberService memberService;

    public MemberUserDetailsServiceImpl(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public Member getMember(UserDetailsRequest request) {
        if (isInvalidRequest(request)) {
            return null;
        }

        Member member = memberService.findMemberByEmailOrThrow(request.getEmail());
        if (!member.getPassword().equals(request.getPassword())) {
            throw new AuthenticationException();
        }
        return member;
    }

    private boolean isInvalidRequest(UserDetailsRequest request) {
        return request.getEmail() == null || request.getPassword() == null;
    }
}
