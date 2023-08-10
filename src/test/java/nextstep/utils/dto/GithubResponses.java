package nextstep.utils.dto;

import java.util.Arrays;

public enum GithubResponses {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com");

    private String code;
    private String accessToken;
    private String email;

    GithubResponses(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static String findAccessTokenByCode(String code) {
        return Arrays.stream(GithubResponses.values())
                .filter(githubResponse -> githubResponse.code.equals(code))
                .map(githubResponses -> githubResponses.accessToken)
                .findFirst().orElseThrow();
    }

    public static String findEmailByAccessToken(String accesstoken) {
        String token = accesstoken.split(" ")[1];
        return Arrays.stream(GithubResponses.values())
                .filter(githubResponses -> githubResponses.accessToken.equals(token))
                .map(githubResponses -> githubResponses.email)
                .findFirst().orElseThrow();
    }
}
