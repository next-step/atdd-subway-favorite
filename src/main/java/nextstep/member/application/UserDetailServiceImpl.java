package nextstep.member.application;

import nextstep.auth.application.UserDetail;
import nextstep.auth.application.UserDetailService;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailService {

    private final MemberRepository memberRepository;

    public UserDetailServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetail loadUser(String userId) {
        return memberRepository.findByEmail(userId)
                .map(member -> new UserDetail(member.getEmail(), member.getPassword()))
                .orElse(UserDetail.EMPTY);
    }

}
