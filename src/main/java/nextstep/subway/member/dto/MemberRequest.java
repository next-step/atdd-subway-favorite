package nextstep.subway.member.dto;

import nextstep.subway.member.domain.Member;

public class MemberRequest {
    private String email;
    private String password;
    private String name;

    public MemberRequest() {
    }

    public MemberRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Member toMember() {
        return new Member(email, password, name);
    }
}
