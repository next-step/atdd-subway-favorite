package subway.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum GithubResponses {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com");

    private final String code;
    private final String accessToken;
    private final String email;

    public static GithubResponses findByCode(final String code){
        return Arrays.stream(values()).filter(value -> value.getCode().equals(code)).findAny().orElse(null);
    }

    public static GithubResponses findByAccessToken(final String accessToken){
        return Arrays.stream(values()).filter(value -> value.getAccessToken().equals(accessToken)).findAny().orElse(null);
    }
}
