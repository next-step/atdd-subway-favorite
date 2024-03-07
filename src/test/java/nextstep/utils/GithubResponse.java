package nextstep.utils;

import nextstep.member.AuthenticationException;

import java.util.Arrays;
import java.util.Objects;

public enum GithubResponse {
    회원("aofijeowifjaoief", "access_token_1", "email1@email.com", 20),
    토큰_없는_회원("fau3nfin93dmn", null, null, 0),
    사용자_정보_없음("afnm93fmdodf", "access_token_3", null, 0),
    비회원("fm04fndkaladmd", "access_token_4", "email4@email.com", 35);

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

    public static GithubResponse ofCode(final String code) {
        return Arrays.stream(values())
                .filter(u -> u.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new AuthenticationException("code에 해당하는 사용자 정보가 없습니다."));
    }

    public static GithubResponse ofToken(final String accessToken) {
        return Arrays.stream(values())
                .filter(u -> Objects.equals(accessToken, u.accessToken))
                .findFirst()
                .orElseThrow(() -> new AuthenticationException("access token에 해당하는 사용자 정보가 없습니다."));
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
