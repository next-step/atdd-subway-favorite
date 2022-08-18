package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import nextstep.MemberData;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.test.util.ReflectionTestUtils;

import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;

public class SecurityUtil {

    public static RequestSpecification given() {
        String token = 로그인_되어_있음(MemberData.admin.getEmail(), MemberData.admin.getPassword());

        return RestAssured.given().log().all()
                .auth().oauth2(token);
    }

    public static JwtTokenProvider getUnlimitedJwtTokenProvider() {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "atdd-secret-key");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", Integer.MAX_VALUE);

        return jwtTokenProvider;
    }
}