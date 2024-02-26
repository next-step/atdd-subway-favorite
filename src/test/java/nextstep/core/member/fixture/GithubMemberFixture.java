package nextstep.core.member.fixture;

import nextstep.common.exception.BadRequestException;

import java.util.Arrays;
import java.util.List;

public enum GithubMemberFixture {
    KIM("kim_code", "Bearer K_TOKEN", "kim_email@email.com"),
    HWANG("hwang_code", "Bearer H_TOKEN", "hwang_email@email.com"),
    JUNG("jung_code", "Bearer J_TOKEN", "jung_email@email.com"),
    LEE("lee_code", "Bearer L_TOKEN", "lee_email@email.com");

    public final String code;
    public final String token;
    public final String email;

    GithubMemberFixture(String code, String token, String email) {
        this.code = code;
        this.token = token;
        this.email = email;
    }

    public static String findTokenByCode(String code) {
        return Arrays.stream(values())
                .filter(tokenFixture -> tokenFixture.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("허용되지 않는 코드입니다."))
                .getToken();
    }

    public static String findMemberByToken(String token) {
        return Arrays.stream(values())
                .filter(tokenFixture -> tokenFixture.getToken().equals(token))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("허용되지 않는 토큰입니다."))
                .getEmail();
    }

    public String getCode() {
        return code;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public static List<GithubMemberFixture> getAllGitHubMembers() {
        return List.of(values());
    }
}
