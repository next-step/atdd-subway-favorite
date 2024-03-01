package nextstep.member.application;

import nextstep.auth.application.UserDetailsService;
import nextstep.auth.domain.UserDetail;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberService memberService;

    public UserDetailsServiceImpl(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public UserDetail getUser(String email) {
        Member member = memberService.findMemberByEmail(email);

        return new UserDetail(member.getEmail(), member.getPassword(), member.getAge());
    }

    @Override
    public UserDetail getGithubUser(String email, Integer age) {
        Member member = memberService.findMemberByEmailAndAge(email, age);

        return new UserDetail(member.getEmail(), member.getPassword(), member.getAge());
    }
}
