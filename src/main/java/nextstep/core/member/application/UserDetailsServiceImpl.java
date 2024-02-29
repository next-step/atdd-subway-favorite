package nextstep.core.member.application;

import nextstep.core.auth.application.UserDetailsService;
import nextstep.core.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    MemberService memberService;

    public UserDetailsServiceImpl(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean verifyUser(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.checkPassword(password)) {
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }
        return true;
    }

    @Override
    public String findOrCreate(String email) {
        Member member = memberService.findOrCreate(email);
        return member.getEmail();
    }
}
