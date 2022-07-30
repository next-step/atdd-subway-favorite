package nextstep.member.application;

import nextstep.auth.userdetails.UserDetails;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.NotFoundMemberException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements nextstep.auth.userdetails.UserDetailsService {
    private final MemberRepository memberRepository;

    public UserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(NotFoundMemberException::new);
        return LoginMember.of(member);
    }
}
