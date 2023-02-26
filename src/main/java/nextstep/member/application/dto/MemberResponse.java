package nextstep.member.application.dto;

import lombok.Getter;
import nextstep.member.domain.Member;

@Getter
public class MemberResponse {
    private final Long id;
    private final String email;
    private final Integer age;

    private MemberResponse(final Long id, final String email, final Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static MemberResponse of(final Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getAge());
    }
}
