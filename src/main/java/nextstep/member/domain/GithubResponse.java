package nextstep.member.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum GithubResponse {

    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com");

    private String code;
    private String accessToken;
    private String email;

    GithubResponse(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static String getAccessTokenByCode(String code) {
        return Arrays.stream(GithubResponse.values())
                .filter(x -> x.getCode().equals(code))
                .map(GithubResponse::getAccessToken)
                .findAny().get();
    }

    public static String getEmailByAccessToken(String accessToken) {
        return Arrays.stream(GithubResponse.values())
                .filter(x -> x.getAccessToken().equals(accessToken))
                .map(GithubResponse::getEmail)
                .findAny().get();
    }

}
