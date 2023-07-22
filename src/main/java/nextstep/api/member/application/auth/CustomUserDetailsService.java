package nextstep.api.member.application.auth;

import org.springframework.stereotype.Service;

import nextstep.api.auth.AuthenticationException;
import nextstep.api.auth.application.userdetails.UserDetails;
import nextstep.api.auth.application.userdetails.UserDetailsService;
import nextstep.api.member.domain.Member;
import nextstep.api.member.domain.MemberRepository;

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
