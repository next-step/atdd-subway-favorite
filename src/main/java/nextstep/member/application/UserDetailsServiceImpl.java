package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.UserDetails;
import nextstep.auth.application.UserDetailsService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails findMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(RuntimeException::new);

        return new UserDetails(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
    }

    @Override
    public UserDetails findOrCreateMember(String email, Integer age) {
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(new Member(email, UUID.randomUUID().toString(), age)));

        return new UserDetails(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
    }
}
