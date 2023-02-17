package nextstep.member.infrastructure.dto;

import nextstep.member.domain.Member;

public class MemberInfo {
    private Long id;

    public MemberInfo() {
    }

    public MemberInfo(Long id) {
        this.id = id;
    }

    public static MemberInfo from(Member member) {
        return new MemberInfo(member.getId());
    }

    public Long getId() {
        return id;
    }
}
