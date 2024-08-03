package nextstep.subway.github.application;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.application.dto.UserDetailsRequest;
import nextstep.subway.exception.NoSuchMemberException;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.application.dto.MemberRequest;
import nextstep.subway.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class GithubUserDetailsServiceImpl implements UserDetailsService {
    private MemberService memberService;

    public GithubUserDetailsServiceImpl(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public Member getMember(UserDetailsRequest request) {
        if (isInvalidRequest(request)) {
            return null;
        }

        return findOrCreateMember(request.getEmail());
    }

    private boolean isInvalidRequest(UserDetailsRequest request) {
        return request.getEmail() == null || request.getPassword() != null;
    }

    private Member findOrCreateMember(String email) {
        Member member;
        try {
            member = memberService.findMemberByEmailOrThrow(email);
        } catch (NoSuchMemberException e) {
            memberService.createMember(new MemberRequest(email));
            member = memberService.findMemberByEmailOrThrow(email);
        }
        return member;
    }
}
