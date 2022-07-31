package nextstep.subway.acceptance;

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

    private static final String MEMBER_EMAIL = "my@email.com";

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        ExtractableResponse<Response> response = 회원_생성(MEMBER_EMAIL, PASSWORD, AGE);
        final Long id = 회원_ID_조회(response);

        // then
        회원가입성공(response, id);
    }

    private void 회원가입성공(final ExtractableResponse<Response> response, final long id) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(회원_정보_조회(id).jsonPath().getLong("id")).isNotNull();
    }

    @DisplayName("나의 회원 정보를 조회한다.")
    @Test
    void getMemberMine() {
        // given
        회원_생성(MEMBER_EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 나의_회원_정보_조회(MEMBER_EMAIL);

        // then
        회원_정보_조회됨(response, MEMBER_EMAIL, AGE);
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        final ExtractableResponse<Response> createResponse = 회원_생성(MEMBER_EMAIL, PASSWORD, AGE);
        final Long id = 회원_ID_조회(createResponse);

        // when
        ExtractableResponse<Response> response = 회원_정보_조회(id);

        // then
        회원_정보_조회됨(response, MEMBER_EMAIL, AGE);

    }

    private ExtractableResponse<Response> 나의_회원_정보_조회(final String email) {
        return adminGiven(사용자Bearer토큰(email))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    @DisplayName("나의 회원 정보를 수정한다.")
    @Test
    void updateMemberMine() {
        // given
        회원_생성(MEMBER_EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 나의_회원_정보_수정_요청("new" + MEMBER_EMAIL, MEMBER_EMAIL);

        // then
        회원정보수정성공(response);
    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        final ExtractableResponse<Response> createResponse = 회원_생성(MEMBER_EMAIL, PASSWORD, AGE);
        final Long id = 회원_ID_조회(createResponse);

        // when
        ExtractableResponse<Response> response = 회원_정보_수정(id, "new" + MEMBER_EMAIL, "new" + PASSWORD, AGE);

        // then
        회원정보수정성공(response);
    }

    private ExtractableResponse<Response> 나의_회원_정보_수정_요청(String newEmail, String email) {
        Map<String, String> params = new HashMap<>();
        params.put("email", newEmail);
        params.put("password", PASSWORD);
        params.put("age", AGE + "");

        return adminGiven(사용자Bearer토큰(email))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/members/me")
                .then().log().all().extract();
    }

    private void 회원정보수정성공(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("email")).isEqualTo("new" + MEMBER_EMAIL);
    }

    @DisplayName("나의 회원 정보를 삭제한다.")
    @Test
    void deleteMemberMine() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성(MEMBER_EMAIL, PASSWORD, AGE);
        final Long id = 회원_ID_조회(createResponse);

        // when
        ExtractableResponse<Response> response = 나의_회원_삭제_요청(MEMBER_EMAIL);

        // then
        회원정보삭제성공(response, id);
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        final ExtractableResponse<Response> createResponse = 회원_생성(MEMBER_EMAIL, PASSWORD, AGE);
        final Long id = 회원_ID_조회(createResponse);

        // when
        ExtractableResponse<Response> response = 회원_삭제(id);

        // then
        회원정보삭제성공(response, id);
    }

    private ExtractableResponse<Response> 나의_회원_삭제_요청(final String email) {
        return adminGiven(사용자Bearer토큰(email))
                .when().delete("/members/me")
                .then().log().all().extract();
    }

    private void 회원정보삭제성공(final ExtractableResponse<Response> response, final Long id) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(회원_정보_조회(id).statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

}