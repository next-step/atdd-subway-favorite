package nextstep.member.application.dto;

import nextstep.auth.application.dto.GithubMemberRequest;
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

    public static MemberRequest of(GithubMemberRequest githubMemberRequest) {
        return new MemberRequest(githubMemberRequest.getEmail(), null, githubMemberRequest.getAge());
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
