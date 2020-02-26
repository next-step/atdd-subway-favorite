package atdd.path.application.dto;

import atdd.path.domain.Member;

public class MemberResponseView {

    private Long id;
    private String email;
    private String name;
    private String password;

    private MemberResponseView() {}

    public MemberResponseView(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.password = member.getPassword();
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

    public String getPassword() {
        return password;
    }

}
