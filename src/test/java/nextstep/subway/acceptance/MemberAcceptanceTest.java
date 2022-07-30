package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        ExtractableResponse<Response> response = 회원_생성_요청(ADMIN_EMAIL, PASSWORD, AGE);

        // then
        회원가입성공(response);
    }

    private void 회원가입성공(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        final long id = 회원_정보_조회_요청(response).jsonPath().getLong("id");
        assertThat(회원_정보_조회(id).jsonPath().getLong("id")).isNotNull();
    }

    @DisplayName("나의 회원 정보를 조회한다.")
    @Test
    void getMemberMine() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(ADMIN_EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회됨(response, ADMIN_EMAIL, AGE);
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        final ExtractableResponse<Response> 회원_생성_요청 = 회원_생성_요청(ADMIN_EMAIL, PASSWORD, AGE);
        final Long id = 회원_정보_조회_요청(회원_생성_요청).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 회원_정보_조회(id);

        // then
        회원_정보_조회됨(response, ADMIN_EMAIL, AGE);

    }

    @DisplayName("나의 회원 정보를 수정한다.")
    @Test
    void updateMemberMine() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(ADMIN_EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_정보_수정_요청(createResponse, "new" + ADMIN_EMAIL, "new" + PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("email")).isEqualTo("new" + ADMIN_EMAIL);
    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        final ExtractableResponse<Response> 회원_생성_요청 = 회원_생성_요청(ADMIN_EMAIL, PASSWORD, AGE);
        final Long id = 회원_정보_조회_요청(회원_생성_요청).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 회원_정보_수정(id, "new" + ADMIN_EMAIL, "new" + PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("email")).isEqualTo("new" + ADMIN_EMAIL);
    }

    @DisplayName("나의 회원 정보를 삭제한다.")
    @Test
    void deleteMemberMine() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(ADMIN_EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);

        // then
        회원정보삭제성공(response, createResponse);
    }

    private void 회원정보삭제성공(final ExtractableResponse<Response> response, final ExtractableResponse<Response> createResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(회원_정보_조회_요청(createResponse).statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        final ExtractableResponse<Response> 회원_생성_요청 = 회원_생성_요청(ADMIN_EMAIL, PASSWORD, AGE);
        final Long id = 회원_정보_조회_요청(회원_생성_요청).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 회원_삭제(id);

        // then
        회원정보삭제성공(response, id);
    }

    private void 회원정보삭제성공(final ExtractableResponse<Response> response, final Long id) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(회원_정보_조회(id).statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
    }

    private ExtractableResponse<Response> 회원_정보_조회(final Long id) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/{id}", id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 회원_정보_수정(Long id, String email, String password, Integer age) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age + "");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/members/{id}", id)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 회원_삭제(Long id) {
        return RestAssured
                .given().log().all()
                .when().delete("/members/{id}", id)
                .then().log().all().extract();
    }

}