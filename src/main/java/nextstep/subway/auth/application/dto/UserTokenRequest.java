package nextstep.subway.auth.application.dto;

public class UserTokenRequest {
    private String email;
    private String password;
    private String code;

    private UserTokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private UserTokenRequest(String code) {
        this.code = code;
    }

    public static UserTokenRequest fromMemberTokenRequest(MemberTokenRequest request) {
        return new UserTokenRequest(request.getEmail(), request.getPassword());
    }

    public static UserTokenRequest fromGithubTokenRequest(GithubTokenRequest request) {
        return new UserTokenRequest(request.getCode());
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }
}
