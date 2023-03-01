package nextstep.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.member.domain.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthMember {

    private Long id;
    private String email;
    private Integer age;

    public static AuthMember of(Member member) {
        return new AuthMember(member.getId(), member.getEmail(), member.getAge());
    }
}
