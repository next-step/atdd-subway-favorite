package nextstep.auth;

import java.util.Arrays;

public enum GithubResponses {

    USER_A("sdfoiwjeflkwej", "usera@naver.com", "token_aaa"),
    USER_B("fwefsdfsdlkji", "userb@naver.com", "token_bbb"),
    USER_C("wefwefefef123", "userc@naver.com", "token_ccc"),
    UNAUTHORIZED_USER("xx", "xx", "xx")
    ;
    private String code;
    private String email;
    private String token;

    GithubResponses(String code, String email, String token) {
        this.code = code;
        this.email = email;
        this.token = token;
    }

    public static GithubResponses getCode(String email) {
        return Arrays.stream(GithubResponses.values())
                .filter(gr -> gr.email.equals(email))
                .findFirst()
                .orElse(UNAUTHORIZED_USER);
    }

    public static String getEmail(String code) {
        return Arrays.stream(GithubResponses.values())
                .filter(gr -> gr.code.equals(code))
                .findFirst()
                .orElse(UNAUTHORIZED_USER)
                .email;
    }

    public static String getEmailOfToken(String token) {
        return Arrays.stream(GithubResponses.values())
                .filter(gr -> gr.token.equals(token))
                .findFirst()
                .orElse(UNAUTHORIZED_USER)
                .email;
    }

    public String getToken() {
        return token;
    }

    public String getCode() {
        return code;
    }

    public String getEmail() {
        return email;
    }

    public boolean isUnAuthorized() {
        return this == UNAUTHORIZED_USER;
    }
}
