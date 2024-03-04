package nextstep.auth;

import nextstep.exception.SubwayException;

import java.util.Arrays;

public enum GithubResponse {

    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", 20),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", 21),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 22),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", 23);

    private String code;
    private String accessToken;
    private String email;
    private int age;

    GithubResponse(String code, String accessToken, String email, int age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.age = age;
    }

    public static GithubResponse findByCode(String code) {
        return Arrays.stream(values())
                .filter(member -> member.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new SubwayException("유효하지 않은 코드입니다."));
    }

    public static GithubResponse findByAccessToken(String accessToken) {
        return Arrays.stream(values())
                .filter(member -> member.getAccessToken().equals(accessToken))
                .findFirst()
                .orElseThrow(() -> new SubwayException("유효하지 않은 토큰입니다."));
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

    public int getAge() {
        return age;
    }
}
