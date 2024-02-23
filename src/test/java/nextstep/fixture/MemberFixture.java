package nextstep.fixture;

import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.utils.MemberTestUtil.회원_로그인_요청;

public class MemberFixture {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 30;
    private final String accessToken;

    public MemberFixture() {
        회원_생성_요청(EMAIL, PASSWORD, AGE).header("location");
        this.accessToken = 회원_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }

    public String getAccessToken() {
        return accessToken;
    }

}