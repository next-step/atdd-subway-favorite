package nextstep.fixture;


import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.fixture.FieldFixture.Access_Token;
import static nextstep.fixture.MemberFixture.관리자_ADMIN;
import static nextstep.fixture.MemberFixture.회원_ALEX;
import static nextstep.utils.JsonPathUtil.문자열로_추출;

public enum AuthFixture {
    알렉스(회원_ALEX),
    어드민(관리자_ADMIN),
    ;

    private final MemberFixture memberFixture;

    AuthFixture(MemberFixture memberFixture) {
        this.memberFixture = memberFixture;
    }

    public MemberFixture 회원_정보() {
        return memberFixture;
    }

    public static ExtractableResponse<Response> 베어러_인증_로그인_요청(AuthFixture 인증_주체) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(인증_주체.회원_정보().로그인_요청_데이터_생성())
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
    }

    public static RequestSpecification 로그인된_상태(AuthFixture 인증_주체) {
        ExtractableResponse<Response> 베어러_인증_로그인_결과 = 베어러_인증_로그인_요청(인증_주체);

        return RestAssured.requestSpecification = new RequestSpecBuilder()
                .setAccept(MediaType.APPLICATION_JSON_VALUE)
                .setContentType(MediaType.APPLICATION_JSON_VALUE)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + 문자열로_추출(베어러_인증_로그인_결과, Access_Token))
                .build();
    }
}
