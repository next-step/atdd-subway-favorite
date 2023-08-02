package nextstep.member.service;

import lombok.RequiredArgsConstructor;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;
import nextstep.member.adapters.persistence.MemberAdapter;
import nextstep.member.entity.CustomUserDetails;
import nextstep.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberAdapter memberAdapter;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = memberAdapter.findByEmail(username);
        return CustomUserDetails.of(member);
    }
}
