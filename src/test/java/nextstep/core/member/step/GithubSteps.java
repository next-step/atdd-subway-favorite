package nextstep.core.member.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.core.member.application.dto.GithubCodeRequest;
import nextstep.core.member.fixture.GithubMemberFixture;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GithubSteps {

    public static void 깃허브_회원가입(GithubMemberFixture... githubMember) {
        Arrays.stream(githubMember).forEach(member -> {
            토큰_확인(깃허브_로그인_요청(member, HttpStatus.OK));
        });
    }

    public static String 깃허브_로그인_요청(GithubCodeRequest request) {
            return 깃허브_로그인_요청(request, HttpStatus.BAD_REQUEST);
    }

    public static List<String> 깃허브_로그인_요청(GithubMemberFixture... githubMember) {
        List<String> list = new ArrayList<>();
        Arrays.stream(githubMember).forEach(member -> {
            list.add(깃허브_로그인_요청(member, HttpStatus.OK));
        });
        return list;
    }

    private static String 깃허브_로그인_요청(GithubMemberFixture member, HttpStatus httpStatus) {
        ExtractableResponse<Response> 토큰_응답 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new GithubCodeRequest(member.getCode()))
                .when()
                .post("/login/github")
                .then().log().all()
                .statusCode(httpStatus.value()).extract();
        return 토큰_추출(토큰_응답);
    }

    private static String 깃허브_로그인_요청(GithubCodeRequest request, HttpStatus httpStatus) {
        ExtractableResponse<Response> 응답 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new GithubCodeRequest(request.getCode()))
                .when()
                .post("/login/github")
                .then().log().all()
                .statusCode(httpStatus.value()).extract();
        return 토큰_추출(응답);
    }

    public static void 토큰_확인(List<String> 발급된_토큰_목록) {
        발급된_토큰_목록.forEach(발급된_토큰 -> {
            assertThat(발급된_토큰).isNotBlank();
        });
    }

    public static void 토큰_확인(String 발급된_토큰) {
        assertThat(발급된_토큰).isNotBlank();
    }

    public static void 토큰_없음_확인(String 깃허브_로그인_요청) {
        Assertions.assertThat(깃허브_로그인_요청).isBlank();
    }

    private static String 토큰_추출(ExtractableResponse<Response> 토큰_응답) {
        return 토큰_응답.jsonPath().getString("accessToken");
    }
}
