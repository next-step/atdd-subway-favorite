package atdd.path.application.dto;

import atdd.path.domain.Member;

import java.util.StringJoiner;

public class CreateMemberRequestView {

    private String email;
    private String name;
    private String password;

    protected CreateMemberRequestView() {}

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Member toMember() {
        return new Member(this.email, this.name, this.password);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CreateMemberRequestView.class.getSimpleName() + "[", "]")
                .add("email='" + email + "'")
                .add("name='" + name + "'")
                .add("password='" + password + "'")
                .toString();
    }

}
