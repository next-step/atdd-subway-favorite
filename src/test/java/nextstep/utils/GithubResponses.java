package nextstep.utils;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static nextstep.common.Constant.*;

public enum GithubResponses {

    사용자_홍길동("aofijeowifjaoief", "access_token_1", 홍길동_이메일),
    사용자_임꺽정("fau3nfin93dmn", "access_token_2", 임꺽정_이메일),
    사용자_없음("", "", "");

    private String code;
    private String accessToken;
    private String email;

    GithubResponses(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static String getAccessTokenByCode(String code) {
        return Arrays.stream(GithubResponses.values())
                .filter(githubResponses -> Objects.equals(githubResponses.getCode(), code))
                .map(githubResponses -> githubResponses.getAccessToken())
                .findFirst()
                .orElse(GithubResponses.사용자_없음.getAccessToken());
    }

    public static GithubResponses getGithubResponseByAccessToken(String accessToken) {
        return Arrays.stream(GithubResponses.values())
                .filter(githubResponses -> Objects.equals(githubResponses.getAccessToken(), accessToken))
                .findFirst()
                .orElse(GithubResponses.사용자_없음);
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

}
