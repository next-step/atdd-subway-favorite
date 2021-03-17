package nextstep.subway.member.dto;

import nextstep.subway.member.domain.Member;

public class MemberResponse {
    private Long id;
    private String email;
    private String name;

    public MemberResponse() {
    }

    public MemberResponse(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getName());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}