package nextstep.member.service;

import lombok.RequiredArgsConstructor;
import nextstep.auth.userdetails.CustomUserDetails;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;
import nextstep.member.adapters.persistence.MemberJpaAdapter;
import nextstep.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberJpaAdapter memberJpaAdapter;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = memberJpaAdapter.loginByEmail(username);
        return CustomUserDetails.of(member);
    }
}
