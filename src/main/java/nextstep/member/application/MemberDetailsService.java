package nextstep.member.application;

import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;
import nextstep.member.adapter.MemberRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberDetails;
import org.springframework.stereotype.Service;

@Service
public class MemberDetailsService implements UserDetailsService {
    private MemberRepository memberRepository;

    public MemberDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return MemberDetails.of(member);
    }
}
