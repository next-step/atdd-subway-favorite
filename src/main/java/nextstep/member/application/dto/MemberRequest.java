package nextstep.member.application.dto;

import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;

public class MemberRequest {
    private String email;
    private String password;
    private Integer age;

    public MemberRequest() {
    }

    public MemberRequest(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public static MemberRequest of(LoginMember loginMember) {

        return new MemberRequest(loginMember.getEmail(), loginMember.getPassword(), loginMember.getAge());
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }

    public Member toMember() {
        return new Member(email, password, age);
    }
}
