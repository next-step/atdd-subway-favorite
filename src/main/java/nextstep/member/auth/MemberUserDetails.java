package nextstep.member.auth;

import lombok.AllArgsConstructor;
import nextstep.auth.domain.UserDetails;
import nextstep.member.domain.Member;

@AllArgsConstructor
public class MemberUserDetails implements UserDetails {
    private final Member member;

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }
}
