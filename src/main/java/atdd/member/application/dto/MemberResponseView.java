package atdd.member.application.dto;

import atdd.member.domain.Member;

public class MemberResponseView {

    private long id;
    private String email;
    private String name;

    public MemberResponseView() {
    }

    public MemberResponseView(long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static MemberResponseView of(Member member) {
        return new MemberResponseView(member.getId(), member.getEmail(), member.getName());
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

}
