package nextstep.subway.fake;

import java.util.Arrays;

public enum GithubFakeResponses {

    사용자1(1L, "832ovnq039hfjn", "access_token_1", "email@email.com"),
    사용자2(2L, "mkfo0aFa03m", "access_token_2", "email2@email.com"),
    사용자3(3L, "m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    사용자4(4L, "nvci383mciq0oq", "access_token_4", "email4@email.com");

    private final Long id;
    private final String code;
    private final String accessToken;
    private final String email;

    GithubFakeResponses(Long id, String code, String accessToken, String email) {
        this.id = id;
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static GithubFakeResponses fromCode(String code) {
        return Arrays.stream(values())
                .filter(it -> it.code.equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static GithubFakeResponses fromAccessToken(String accessToken) {
        return Arrays.stream(values())
                .filter(it -> it.accessToken != null && it.accessToken.equals(accessToken))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Long getId() {
        return id;
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
