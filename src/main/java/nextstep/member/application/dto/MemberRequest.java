package nextstep.member.application.dto;

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

    public MemberRequest(String email) {
        this.email = email;
    }

    public Member toMember() {
        return new Member(email, password, age);
    }

    public static MemberRequest from(GithubProfileResponse profileResponse) {
        return new MemberRequest(profileResponse.getEmail());
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
}
