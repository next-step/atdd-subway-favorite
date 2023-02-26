package nextstep.subway.fake;

import java.util.Arrays;
import nextstep.member.auth.OAuth2User;

public enum GithubResponses implements OAuth2User {
    사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
    사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com"),
    ;

    private String code;
    private String accessToken;
    private String email;


    GithubResponses(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static OAuth2User findUserByAccessToken(String accessToken) {
        return Arrays.stream(values())
            .filter(it -> accessToken.equals(it.accessToken))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException());
    }

    public static String findAccessCodeByCode(String code) {
        return Arrays.stream(values())
            .filter(it -> it.code.equals(code))
            .map(it -> it.accessToken)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException());
    }

    @Override
    public String getName() {
        return email;
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
