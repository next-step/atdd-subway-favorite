package nextstep.auth.domain;

import nextstep.member.domain.Member;

public class LoginUserInfo {

    private Long memberId;
    private String email;
    private Integer age;

    private LoginUserInfo() {}

    public LoginUserInfo(final Long memberId, final String email, final Integer age) {
        this.memberId = memberId;
        this.email = email;
        this.age = age;
    }

    public static LoginUserInfo from(final Member member) {
        return new LoginUserInfo(member.getId(), member.getEmail(), member.getAge());
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
