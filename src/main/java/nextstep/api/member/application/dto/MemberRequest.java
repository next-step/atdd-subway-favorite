package nextstep.api.member.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.api.member.domain.Member;

@Getter
@AllArgsConstructor
public class MemberRequest {
    private String email;
    private String password;
    private Integer age;


    public Member toMember() {
        return new Member(email, password, age);
    }
}
