package nextstep.member.application.dto;

import lombok.Builder;
import lombok.Getter;
import nextstep.member.domain.Member;

@Getter
public class MemberRequest {
    private String email;
    private String password;
    private Integer age;

    public MemberRequest() {
    }

    @Builder
    public MemberRequest(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Member toMember() {
        return new Member(email, password, age);
    }
}
