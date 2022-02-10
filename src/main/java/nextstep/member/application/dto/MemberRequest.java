package nextstep.member.application.dto;

import nextstep.member.domain.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class MemberRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @Positive
    private Integer age;

    public MemberRequest() {
    }

    public MemberRequest(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
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
