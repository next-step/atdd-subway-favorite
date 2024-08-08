package nextstep.member.infrastructure;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.UserDetails;
import nextstep.auth.domain.UserDetailsService;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadByUserEmail(String email, int age) {
        Member member = memberService.findMemberByUserResource(email, age);
        return new LoginMember(member.getId(), member.getEmail(), member.getPassword());
    }

    @Override
    public UserDetails loadByUserEmail(String email) {
        Member member = memberService.findMemberByEmail(email);
        return new LoginMember(member.getId(), member.getEmail(), member.getPassword());
    }
}
