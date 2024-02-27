package nextstep.core.member.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.core.member.application.dto.MemberRequest;
import nextstep.core.auth.fixture.GithubMemberFixture;
import nextstep.core.member.fixture.MemberFixture;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberSteps {

    public static ExtractableResponse<Response> 회원_생성_요청(String 이메일, String 비밀번호, int 나이) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MemberRequest(이메일, 비밀번호, 나이))
                .when().post("/members")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(response.header("Location"))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> 응답, String 이메일, String 비밀번호, Integer 나이) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MemberRequest(이메일, 비밀번호, 나이))
                .when().put(응답.header("Location"))
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 회원_생성_요청(MemberFixture memberFixture) {
        return 회원_생성_요청(memberFixture.이메일, memberFixture.비밀번호, memberFixture.나이);
    }

    public static ExtractableResponse<Response> 회원_생성_요청(GithubMemberFixture githubMemberFixture, String password, int age) {
        return 회원_생성_요청(githubMemberFixture.get이메일(), password, age);
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        return RestAssured
                .given().log().all()
                .when().delete(response.header("Location"))
                .then().log().all().extract();
    }

    private static ExtractableResponse<Response> 토큰으로_회원정보_요청(String 발급받은_토큰, HttpStatus httpStatus) {
        return RestAssured.given().log().all()
                .auth().oauth2(발급받은_토큰)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(httpStatus.value()).extract();
    }

    public static ExtractableResponse<Response> 성공하는_토큰으로_회원정보_요청(String 발급받은_토큰) {
        return 토큰으로_회원정보_요청(발급받은_토큰, HttpStatus.OK);
    }

    public static void 실패하는_토큰으로_회원정보_요청(String 발급받은_토큰) {
        토큰으로_회원정보_요청(발급받은_토큰, HttpStatus.UNAUTHORIZED);
    }

    public static void 회원_정보_확인(ExtractableResponse<Response> 회원정보_요청_응답, String 이메일, int 나이) {
        assertThat(회원정보_요청_응답.jsonPath().getString("email")).isEqualTo(이메일);
        assertThat(회원정보_요청_응답.jsonPath().getInt("age")).isEqualTo(나이);
    }
}