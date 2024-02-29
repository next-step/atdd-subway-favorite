package nextstep.auth;

import java.util.Arrays;

public enum GithubResponse {
    사용자1("code1", "access_token1", "user1@email.com", 20),
    사용자2("code2", "access_token2", "user2@email.com", 25),
    사용자3("code3", "access_token3", "user3@email.com", 30);

    private String code;
    private String accessToken;
    private String email;
    private Integer age;

    GithubResponse(String code, String accessToken, String email, Integer age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.age = age;
    }

    public String getCode() {
        return code;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public static GithubResponse findByCode(String code) {
        return Arrays.stream(values()).filter(g -> g.getCode().equals(code)).findFirst()
                .orElseThrow(() -> new AuthenticationException("존재하지 않는 회원입니다."));
    }

    public static GithubResponse findByAccessToken(String accessToken) {
        return Arrays.stream(values()).filter(g -> g.getAccessToken().equals(accessToken)).findFirst()
                .orElseThrow(() -> new AuthenticationException("존재하지 않는 회원입니다."));
    }
}
