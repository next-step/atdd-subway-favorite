package nextstep.subway.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum GithubResponses {
    사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
    사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com"),
    사용자5("asdiofuasoipd", null, "email5@email.com");

    private String code;
    private String accessToken;
    private String email;

    GithubResponses(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
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

    public static GithubResponses getGithubResponsesFromCode(String code) {
        return Arrays.stream(values())
                .filter(githubResponse -> StringUtils.equalsIgnoreCase(githubResponse.getCode(), code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static GithubResponses fromAccessToken(String accessToken) {
        return Arrays.stream(values())
                .filter(githubResponse -> StringUtils.equalsIgnoreCase(githubResponse.getAccessToken(), accessToken))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
