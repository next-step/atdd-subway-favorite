package nextstep.member.tobe.application;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;

@Service
public class DefaultUserDetailsService implements UserDetailsService {
    private final MemberService memberService;

    public DefaultUserDetailsService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberService.findMemberByEmailOrThrow(email);
        return new User(member.getEmail(), member.getPassword(), new ArrayList<>());
    }
}
