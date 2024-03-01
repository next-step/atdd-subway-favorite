package nextstep.auth;

import java.util.Arrays;

public enum GithubResponse {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", 20),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com" , 21),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 22),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", 23);

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
            .orElseThrow(IllegalArgumentException::new);
    }

    public static GithubResponse findByAccessToken(String accessToken) {
        return Arrays.stream(values()).filter(g -> g.getAccessToken().equals(accessToken)).findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
