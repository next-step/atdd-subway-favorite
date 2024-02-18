package nextstep.subway.testhelper.fixture;

import nextstep.subway.testhelper.apicaller.MemberApiCaller;

import java.util.HashMap;
import java.util.Map;

public class MemberFixture {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String EMAIL2 = "email2@email.com";
    private final String accessToken;
    private final String anotherMemberAccessToken;

    public MemberFixture() {
        MemberApiCaller.회원_생성_요청(EMAIL, PASSWORD, AGE).header("location");

        Map<String, String> params = new HashMap<>();
        params.put("email", EMAIL);
        params.put("password", PASSWORD);
        this.accessToken = MemberApiCaller.회원_로그인_요청(params).jsonPath().getString("accessToken");
        MemberApiCaller.회원_생성_요청(EMAIL2, PASSWORD, AGE).header("location");
        params.put("email", EMAIL2);
        this.anotherMemberAccessToken = MemberApiCaller.회원_로그인_요청(params).jsonPath().getString("accessToken");
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getAnotherMemberAccessToken() {
        return anotherMemberAccessToken;
    }
}
