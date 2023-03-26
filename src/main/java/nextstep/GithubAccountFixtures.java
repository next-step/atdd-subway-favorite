package nextstep;

import java.util.Arrays;

import nextstep.member.domain.Member;

public enum GithubAccountFixtures {
    ACCOUNT1("1231@3123", "access1.token", "email1@email.com"),
    ACCOUNT2("3123@1231", "access2.token", "email2@email.com");

    public String code;
    public String accessToken;
    public String email;

    GithubAccountFixtures(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static GithubAccountFixtures findByCode(String code) {
        return Arrays.stream(GithubAccountFixtures.values())
            .filter(data -> data.getCode().equals(code))
            .findFirst()
            .orElseThrow();
    }

    public static GithubAccountFixtures findByAccessToken(String accessToken) {
        return Arrays.stream(GithubAccountFixtures.values())
            .filter(data -> data.getAccessToken().equals(accessToken))
            .findFirst()
            .orElseThrow();
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

    public Member createMember() {
        return new Member(this.email);
    }
}
