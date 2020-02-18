package atdd.member.application.dto;

import atdd.member.domain.Member;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class CreateMemberRequestView {

    @NotNull
    @Email
    private String email;
    @NotNull
    private String name;
    @NotNull
    private String password;

    public CreateMemberRequestView() {
    }

    public CreateMemberRequestView(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Member toMember() {
        return new Member(this.email, this.name, this.password);
    }

    public static CreateMemberRequestView of(String email, String name, String password) {
        return new CreateMemberRequestView(email, name, password);
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
