package nextstep.fake.github;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum GithubStaticUsers {
    깃헙_사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
    깃헙_사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
    깃헙_사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
    깃헙_사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com"),

    SAMPLE1("jabsdhfbehf", "access_token_5", "email5@email.com");

    private final String code;
    private final String accessToken;
    private final String email;

    GithubStaticUsers(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static GithubStaticUsers getByCode(String code) {
        return Arrays.stream(GithubStaticUsers.values())
                .filter(it -> it.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot find github user"));
    }

    public static GithubStaticUsers getByAccessToken(String accessToken) {
        return Arrays.stream(GithubStaticUsers.values())
                .filter(it -> it.getAccessToken().equals(accessToken))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot find github user"));
    }
}

