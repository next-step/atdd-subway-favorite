package nextstep.utils;

import static nextstep.common.Constant.*;

public enum GithubResponses {

    사용자_홍길동("aofijeowifjaoief", "access_token_1", 홍길동_이메일, 홍길동_나이),
    사용자_임꺽정("fau3nfin93dmn", "access_token_2", 임꺽정_이메일, 임꺽정_나이);

    private static final String TOKEN_ISSUE_FAIL_MESSAGE = "ACCESS TOKEN ISSUE FAILED";

    private String code;
    private String accessToken;
    private String email;
    private Integer age;

    GithubResponses(String code, String accessToken, String email, Integer age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.age = age;
    }

    public static String getAccessTokenByCode(String code) {
        for (GithubResponses response : GithubResponses.values()) {
            if (response.getCode().equals(code)) {
                return response.getAccessToken();
            }
        }

        return TOKEN_ISSUE_FAIL_MESSAGE;
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
