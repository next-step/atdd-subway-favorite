package nextstep.member.application.dto;

import nextstep.member.domain.Member;

import java.util.Objects;

public class MemberRequest {
    private String email;
    private String password;
    private Integer age;

    public MemberRequest() {
    }

    public MemberRequest(String email) {
        this.email = email;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberRequest that = (MemberRequest) o;
        return Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, age);
    }
}
