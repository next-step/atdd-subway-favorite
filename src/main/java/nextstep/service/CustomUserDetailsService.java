package nextstep.service;

import nextstep.auth.AuthenticationException;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;
import nextstep.domain.member.CustomUserDetails;
import nextstep.domain.member.Member;
import nextstep.domain.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = memberRepository.findByEmail(username).orElseThrow(AuthenticationException::new);
        return new CustomUserDetails(member.getEmail(), member.getPassword(), member.getRole());
    }
}
