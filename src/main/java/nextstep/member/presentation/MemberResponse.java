package nextstep.member.presentation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;

@RequiredArgsConstructor
@Getter
public class MemberResponse {
    private final Long id;
    private final String email;
    private final Integer age;

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getAge());
    }
}
