package nextstep.member.domain;

import lombok.AllArgsConstructor;
import nextstep.auth.userdetails.UserDetails;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String email;
    private String password;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
