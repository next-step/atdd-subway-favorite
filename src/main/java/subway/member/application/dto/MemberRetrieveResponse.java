package subway.member.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import subway.member.domain.Member;

@Getter
@Builder
@AllArgsConstructor
public class MemberRetrieveResponse {
    private Long id;
    private String email;
    private Integer age;

    public static MemberRetrieveResponse from(Member member) {
        return new MemberRetrieveResponse(member.getId(), member.getEmail(), member.getAge());
    }
}