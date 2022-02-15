package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        ExtractableResponse<Response> response = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * When 회원 생성을 요청
     * Then 회원 생성됨
     * When 회원 정보 조회 요청
     * Then 회원 정보 조회됨
     * When 회원 정보 수정 요청
     * Then 회원 정보 수정됨
     * When 회원 삭제 요청
     * Then 회원 삭제됨
     */

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        //
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> memberResponse = 회원_정보_조회_요청(createResponse);
        ExtractableResponse<Response> newMemberResponse = 회원_정보_수정_요청(createResponse, "New Email", "New Password", AGE);
        ExtractableResponse<Response> deletedMemberResponse = 회원_삭제_요청(createResponse);

        assertThat(memberResponse.jsonPath().getString("email")).isEqualTo(EMAIL);
        assertThat(newMemberResponse.jsonPath().getString("email")).isEqualTo("New Email");
        assertThat(deletedMemberResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        //
    }
}