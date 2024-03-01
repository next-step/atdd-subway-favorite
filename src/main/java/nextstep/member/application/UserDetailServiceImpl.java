package nextstep.member.application;

import nextstep.auth.application.UserDetailService;
import nextstep.auth.domain.UserDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class UserDetailServiceImpl implements UserDetailService {

    private final MemberService memberService;

    public UserDetailServiceImpl(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public UserDetail getUser(String email) {
        return memberService.findMemberByEmail(email);
    }

    @Transactional
    @Override
    public UserDetail getOrCreateUser(String email, String code, Integer age) {
        return memberService.findOrCreateMember(email, code, age);
    }
}
