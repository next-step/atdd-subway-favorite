package nextstep.api.member.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.api.member.domain.Member;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberResponse {
    private Long id;
    private String email;
    private Integer age;

    public MemberResponse(final Long id, final String email, final Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static MemberResponse of(final Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getAge());
    }
}