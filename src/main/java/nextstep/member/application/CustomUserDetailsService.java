package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;
import nextstep.member.MemberNotFoundException;
import nextstep.member.domain.CustomUserDetails;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = memberRepository.findByEmail(username).orElseThrow(MemberNotFoundException::new);
        return new CustomUserDetails(member.getId(), member.getEmail(), member.getPassword());
    }
}
