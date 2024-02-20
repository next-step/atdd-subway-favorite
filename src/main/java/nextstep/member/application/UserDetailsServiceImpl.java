package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.UserDetails;
import nextstep.auth.application.UserDetailsService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails findMemberByEmail(String email) {
        Member member = findByEmail(email)
                .orElseThrow(RuntimeException::new);
        return new UserDetails(member.getId(), member.getPassword(), member.getEmail());
    }

    @Override
    public UserDetails findOrCreate(String email) {
        Member member = findByEmail(email)
                .orElseGet(() -> memberRepository.save(new Member(email)));
        return new UserDetails(member.getId(), member.getPassword(), member.getEmail());
    }

    private Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
