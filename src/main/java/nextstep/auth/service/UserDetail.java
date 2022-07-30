package nextstep.auth.service;

import nextstep.member.domain.Member;

import java.util.List;

public class UserDetail {

    private String email;
    private String password;
    private List<String> authorities;

    private UserDetail() { }

    private UserDetail(String email, String password, List<String> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetail guest() {
        return new UserDetail();
    }

    public static UserDetail of(Member member) {
        return new UserDetail(member.getEmail(), member.getPassword(), member.getRoles());
    }
}
