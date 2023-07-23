package nextstep.utils.github;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;

public enum GithubResponses {
    사용자0("code", "access_token_0", "email0@email.com", 22),
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", 20),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", 30),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 25),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", 28);

    private String code;
    private String accessToken;
    private String email;
    private int age;

    GithubResponses(String code, String accessToken, String email, int age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.age = age;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public static GithubResponses ofCode(String code) {
        return Arrays.stream(GithubResponses.values())
                .filter(githubResponses -> githubResponses.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new DataIntegrityViolationException("코드가 존재하지 않습니다."));
    }

    public static GithubResponses ofAceessToken(String accessToken) {
        return Arrays.stream(GithubResponses.values())
                .filter(githubResponses -> githubResponses.accessToken.equals(accessToken))
                .findFirst()
                .orElseThrow(() -> new DataIntegrityViolationException("토큰이 존재하지 않습니다."));
    }
}
