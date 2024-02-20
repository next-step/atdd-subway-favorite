package nextstep.subway.member.application;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.UserDetail;
import nextstep.subway.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberService memberService;

    public UserDetailsServiceImpl(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public UserDetail findMemberByEmail(String email) {
        Member member = memberService.findMemberByEmail(email);
        return new UserDetail(member.getEmail(), member.getPassword(), member.getAge());
    }

    @Override
    public void findMemberByEmailNotExistSave(String email) {
        memberService.findMemberByEmailNotExistSave(email);
    }
}
