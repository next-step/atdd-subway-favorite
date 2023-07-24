package subway.member.application.dto;

import lombok.Builder;
import lombok.Getter;
import subway.member.domain.Member;
import subway.member.domain.RoleType;

@Getter
@Builder
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

    public Member toInit() {
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .age(this.age)
                .role(RoleType.ROLE_MEMBER)
                .build();
    }
}
