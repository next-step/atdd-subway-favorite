package atdd.member.application.dto;

import atdd.member.domain.Member;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class LoginMemberRequestView {

    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;

    public LoginMemberRequestView() {
    }

    public LoginMemberRequestView(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member toMember() {
        return new Member(this.email, this.password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
