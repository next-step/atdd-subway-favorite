package nextstep.subway.member.domain;

import nextstep.subway.auth.domain.UserDetails;

public class CustomUserDetails implements UserDetails {
    private Long id;
    private String email;
    private String password;

    public CustomUserDetails(){

    }

    public CustomUserDetails(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
