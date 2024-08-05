package nextstep.member.acceptance.test;

import java.util.Arrays;

public enum GithubUser {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", 10),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", 20),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 35),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", 24),
    잘못된사용자("", "", "", null);

    private final String code;
    private final String accessToken;
    private final String email;
    private final Integer age;

    GithubUser(String code, String accessToken, String email, Integer age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.age = age;
    }

    public static String getTokenByCode(String code) {
        GithubUser user = Arrays.stream(GithubUser.values()).filter(githubUser -> githubUser.code.equals(code))
                .findFirst()
                .orElse(GithubUser.잘못된사용자);

        return user.accessToken;
    }

    public static GithubUser findUserByAccessToken(String accessToken) {
        return Arrays.stream(GithubUser.values()).filter(githubUser -> githubUser.accessToken.equals(accessToken))
                .findFirst()
                .orElse(GithubUser.잘못된사용자);
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
}
