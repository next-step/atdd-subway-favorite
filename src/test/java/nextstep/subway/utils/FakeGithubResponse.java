package nextstep.subway.utils;

import nextstep.member.exception.MemberAuthenticationException;

import java.util.Arrays;

public enum FakeGithubResponse {

    사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
    사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");

    private final String code;
    private final String accessToken;
    private final String email;

    FakeGithubResponse(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static String findAccessTokenByCode(String code) {

        System.out.println("code = " + code);
        return Arrays.stream(values())
                .filter(fake -> fake.code.equals(code))
                .findFirst()
                .map(find -> find.accessToken)
                .orElseThrow(MemberAuthenticationException::new);
    }

    public static String findEmailByAccessToken(String accessToken) {
        return Arrays.stream(values())
                .filter(fake -> fake.accessToken.equals(accessToken))
                .findFirst()
                .map(find -> find.email)
                .orElseThrow(MemberAuthenticationException::new);
    }

    public String getCode() {
        return code;
    }

    public String getEmail() {
        return email;
    }
}
