package nextstep.auth.domain;

import nextstep.member.domain.Member;

public class LoginUserInfo {

    private Long id;
    private String email;
    private Integer age;

    private LoginUserInfo() {}

    public LoginUserInfo(final Long id, final String email, final Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginUserInfo from(final Member member) {
        return new LoginUserInfo(member.getId(), member.getEmail(), member.getAge());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
