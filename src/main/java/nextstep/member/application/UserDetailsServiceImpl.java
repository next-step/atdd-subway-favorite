package nextstep.member.application;

import static nextstep.auth.authentication.execption.UsernameNotFoundException.USER_NOT_FOUND;

import nextstep.auth.authentication.execption.UsernameNotFoundException;
import nextstep.auth.userdetails.User;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    public UserDetailsServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return memberRepository.findByEmail(username)
                .map(member -> new User(member.getEmail(), member.getPassword(), member.getRoles()))
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }
}
