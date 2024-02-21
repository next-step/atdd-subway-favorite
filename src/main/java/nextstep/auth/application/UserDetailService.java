package nextstep.auth.application;

import nextstep.auth.domain.UserDetail;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService {
    private MemberRepository memberRepository;

    public UserDetailService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public UserDetail findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(UserDetail::new)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    public UserDetail findOrCreate(String email) {
        final Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(new Member(email)));

        return new UserDetail(member);
    }
}
