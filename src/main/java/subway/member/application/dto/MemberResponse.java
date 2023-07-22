package subway.member.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import subway.member.domain.Member;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String email;
    private Integer age;

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getAge());
    }
}