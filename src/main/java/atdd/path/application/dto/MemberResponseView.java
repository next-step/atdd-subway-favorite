package atdd.path.application.dto;

import atdd.path.domain.Member;

import java.util.StringJoiner;

public class MemberResponseView {

    private Long id;
    private String email;
    private String name;
    private String password;

    protected MemberResponseView() {}

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

    @Override
    public String toString() {
        return new StringJoiner(", ", MemberResponseView.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("email='" + email + "'")
                .add("name='" + name + "'")
                .add("password='" + password + "'")
                .toString();
    }

}
