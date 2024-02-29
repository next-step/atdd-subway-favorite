package nextstep.core.auth.fixture;

import nextstep.common.exception.BadRequestException;

import java.util.Arrays;
import java.util.List;

public enum GithubMemberFixture {
    김영호("kim_code", "Bearer K_TOKEN", "kim_email@email.com"),
    황병국("hwang_code", "Bearer H_TOKEN", "hwang_email@email.com"),
    정다영("jung_code", "Bearer J_TOKEN", "jung_email@email.com"),
    이순금("lee_code", "Bearer L_TOKEN", "lee_email@email.com");

    public final String 코드;
    public final String 토큰;
    public final String 이메일;

    GithubMemberFixture(String 코드, String 토큰, String 이메일) {
        this.코드 = 코드;
        this.토큰 = 토큰;
        this.이메일 = 이메일;
    }

    public static String findTokenByCode(String code) {
        return Arrays.stream(values())
                .filter(tokenFixture -> tokenFixture.get코드().equals(code))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("허용되지 않는 코드입니다."))
                .get토큰();
    }

    public static String findMemberByToken(String token) {
        return Arrays.stream(values())
                .filter(tokenFixture -> tokenFixture.get토큰().equals(token))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("허용되지 않는 토큰입니다."))
                .get이메일();
    }

    public String get코드() {
        return 코드;
    }

    public String get이메일() {
        return 이메일;
    }

    public String get토큰() {
        return 토큰;
    }

    public static List<GithubMemberFixture> 모든_깃허브_회원() {
        return List.of(values());
    }
}
