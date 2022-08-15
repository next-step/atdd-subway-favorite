package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import nextstep.MemberData;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.domain.RoleType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

public class SecurityUtil {

    static JwtTokenProvider jwtTokenProvider = getUnlimitedJwtTokenProvider();
    static String token = jwtTokenProvider.createToken(MemberData.admin.getEmail(), List.of(RoleType.ROLE_ADMIN.toString()));

    public static RequestSpecification given() {
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