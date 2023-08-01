package nextstep.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.member.entity.Member;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberRequest {
    private String email;
    private String password;
    private Integer age;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .age(age)
                .build();
    }
}
