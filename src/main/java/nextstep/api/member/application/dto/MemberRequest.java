package nextstep.api.member.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.api.member.domain.Member;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberRequest {
    private String email;
    private String password;
    private Integer age;

    public MemberRequest(final String email, final String password, final Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Member toMember() {
        return Member.basic(email, password, age);
    }
}
