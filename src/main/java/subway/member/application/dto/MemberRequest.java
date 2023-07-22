package subway.member.application.dto;

import lombok.Getter;
import subway.member.domain.Member;

@Getter
public class MemberRequest {
    private String email;
    private String password;
    private Integer age;

    public Member to() {
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .age(this.age)
                .build();
    }
}