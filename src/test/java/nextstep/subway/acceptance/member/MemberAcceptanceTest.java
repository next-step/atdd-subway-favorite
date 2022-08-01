package nextstep.subway.acceptance.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.member.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    public static final String NEW_EMAIL = "new" + EMAIL;
    public static final String NEW_PASSWORD = "new" + PASSWORD;

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
     * When 신규 회원을 등록하면
     * Then 신규 회원이 등록된다.
     * When 등록된 신규 회원을 조회하면
     * Then 신규 회원 정보가 조회된다.
     * When 회원 정보를 수정하면
     * Then 회원 정보가 수정된다.
     * When 수정된 회원을 조회하면
     * Then 수정된 회원 정보가 조회된다.
     * When 회원 정보를 삭제하면
     * Then 회원 정보가 삭제된다.
     * When 삭제된 회원을 조회하면
     * Then 예외가 발생한다.
     */
    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        // when
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);
        // when
        ExtractableResponse<Response> modifyResponse = 회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, AGE);
        // then
        assertThat(modifyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        // when
        ExtractableResponse<Response> afterModifyResponse = 회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(afterModifyResponse, NEW_EMAIL, AGE);
        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        // when
        ExtractableResponse<Response> afterDeleteResponse = 회원_정보_조회_요청(createResponse);
        // then
        assertThat(afterDeleteResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * When 신규 회원을 등록하면
     * Then 등록된 신규 회원이 생성된다.
     * Given 등록된 신규 회원으로 로그인하고
     * When 내 정보를 조회하면
     * Then 내 정보가 조회된다.
     * When 내 정보를 수정하면
     * Then 내 정보가 수정된다.
     * Given 수정된 내 정보로 다시 로그인하고
     * Then 내 정보를 조회하면 조회된다.
     * When 내 정보를 삭제하면
     * Then 내 정보가 삭제된다.
     * When 삭제된 내 정보를 조회하면
     * Then 예외가 발생한다.
     */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // given
        String accessToken = 로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
        // when
        ExtractableResponse<Response> findResponse = 내_정보_조회_요청(accessToken);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> modifyResponse = 내_정보_수정_요청(accessToken,NEW_EMAIL, NEW_PASSWORD, AGE);
        // then
        assertThat(modifyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // given
        String newAccessToken = 로그인_요청(NEW_EMAIL, NEW_PASSWORD).jsonPath().getString("accessToken");
        // then
        내_정보_조회_검증(newAccessToken, NEW_EMAIL, AGE);

        // when
        ExtractableResponse<Response> deleteResponse = 내_정보_삭제_요청(newAccessToken);
        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // when
        ExtractableResponse<Response> newResponse = 내_정보_조회_요청(newAccessToken);
        // then
        assertThat(newResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}