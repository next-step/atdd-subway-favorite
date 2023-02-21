package nextstep.member.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.member.domain.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoDto {
    private Long id;

    private String email;

    private Integer age;

    public static MemberInfoDto of(Member member) {
        return new MemberInfoDto(member.getId(), member.getEmail(), member.getAge());
    }
}
