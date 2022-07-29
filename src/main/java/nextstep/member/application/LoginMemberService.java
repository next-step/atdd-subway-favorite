package nextstep.member.application;

import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.NotFoundMemberException;
import org.springframework.stereotype.Service;

@Service
public class LoginMemberService {
    private final MemberRepository memberRepository;

    public LoginMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginMember loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(NotFoundMemberException::new);
        return LoginMember.of(member);
    }
}
