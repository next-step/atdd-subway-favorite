package nextstep.member.application.dto;

public class MemberInfoRequest {

    private String email;
    private String password;

    public MemberInfoRequest() {
    }

    public MemberInfoRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
