package nextstep.core.member.fixture;

import nextstep.common.exception.BadRequestException;

import java.util.Arrays;

public enum TokenFixture {
    KIM("K_CODE", "K_TOKEN", "K_EMAIL"),
    HWANG("H_CODE", "H_TOKEN", "H_EMAIL"),
    JUNG("J_CODE", "J_TOKEN", "J_EMAIL"),
    LEE("L_CODE", "L_TOKEN", "L_EMAIL");

    public final String code;
    public final String token;
    public final String email;

    TokenFixture(String code, String token, String email) {
        this.code = code;
        this.token = token;
        this.email = email;
    }

    public static String findToken(String code) {
        return Arrays.stream(values())
                .filter(tokenFixture -> tokenFixture.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("확인되지 않은 코드입니다."))
                .getToken();
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
}
