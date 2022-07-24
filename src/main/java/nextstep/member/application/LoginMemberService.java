package nextstep.member.application;

import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginMemberService {
    private MemberRepository memberRepository;

    public LoginMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginMember loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("회원 정보가 없습니다.")
        );
        return LoginMember.of(member);
    }
}
