package atdd.path.application.dto;

import atdd.path.domain.Member;

import java.util.StringJoiner;

public class MemberResponseView {

    private String email;
    private String name;
    private String password;

    public MemberResponseView(Member member) {
        this.email = member.getEmail();
        this.name = member.getName();
        this.password = member.getPassword();
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
                .add("email='" + email + "'")
                .add("name='" + name + "'")
                .add("password='" + password + "'")
                .toString();
    }

}
