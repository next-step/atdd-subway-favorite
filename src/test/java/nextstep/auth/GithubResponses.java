package nextstep.auth;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GithubResponses {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com");

    private String code;
    private String accessToken;
    private String email;

    public static GithubResponses getUserByCode(String code) {
        return Arrays.stream(GithubResponses.values())
            .filter(user -> user.getCode().equals(code))
            .findFirst()
            .orElse(null);
    }

    public static GithubResponses getUserByAccessToken(String accessToken) {
        return Arrays.stream(GithubResponses.values())
            .filter(user -> user.getAccessToken().equals(accessToken))
            .findFirst()
            .orElse(null);
    }
}
