package nextstep.auth.principal;

public class UserPrincipal {
    private String memberEmail;
    private String role;

    public UserPrincipal(String memberEmail, String role) {
        this.memberEmail = memberEmail;
        this.role = role;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public String getRole() {
        return role;
    }
}
