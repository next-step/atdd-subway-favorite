package nextstep.member.infrastructure.dto;

import nextstep.member.domain.Member;

public class MemberIdDto {
    private Long id;

    public MemberIdDto() {
    }

    public MemberIdDto(Long id) {
        this.id = id;
    }

    public static MemberIdDto from(Member member) {
        return new MemberIdDto(member.getId());
    }

    public Long getId() {
        return id;
    }
}
