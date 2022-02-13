package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.AuthSteps.토큰_인증;
import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Feature : 회원 정보를 관리한다.
 */
class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    private static final String CHANGED_EMAIL = "test@email.com";
    public static final String PASSWORD = "password";
    private static final String CHANGED_PASSWORD = "changedPassword";
    private static final int AGE = 20;
    private static final int CHANGED_AGE = 26;

    /**
     * Scenario     : 회원 정보를 관리한다.
     * <회원 생성>
     * When         : 회원 생성을 요청하면
     * Then         : 회원이 생성된다.
     * <회원 조회>
     * When         : 생성된 회원에 대해 회원 정보 조회를 요청하면
     * Then         : 회원의 정보가 조회된다.
     * <회원 정보 수정>
     * When         : 조회된 회원에 대해 회원 정보 수정을 요청하면
     * Then         : 회원의 정보가 수정된다.
     * <회원 탈퇴>
     * When         : 수정된 회원에 대해 회원 삭제를 요청하면
     * Then         : 회원이 탈퇴된다.
     */
    @DisplayName("(통합) 회원 정보를 관리한다. - 생성, 조회, 수정, 삭제")
    @Test
    void 회원_정보_관리() {
        /* 회원 생성 */
        // when
        ExtractableResponse<Response> 회원생성_response = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        // then
        validateMemberResponse(회원생성_response, HttpStatus.CREATED, EMAIL, AGE);

        /* 회원 조회 */
        // when
        ExtractableResponse<Response> 회원조회_response = 회원_정보_조회_요청(회원생성_response);
        // then
        validateMemberResponse(회원조회_response, HttpStatus.OK, EMAIL, AGE);

        /* 회원 수정 */
        // when
        ExtractableResponse<Response> 회원수정_response =
                회원_정보_수정_요청(회원생성_response, CHANGED_EMAIL, CHANGED_PASSWORD, CHANGED_AGE);
        // then
        validateMemberResponse(회원수정_response, HttpStatus.OK, CHANGED_EMAIL, CHANGED_AGE);

        /* 회원 삭제 */
        // when
        ExtractableResponse<Response> 회원삭제_response = 회원_삭제_요청(회원생성_response);

        // then
        assertThat(회원삭제_response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void validateMemberResponse(ExtractableResponse<Response> response, HttpStatus status, String email, int age) {
        assertThat(response.statusCode()).isEqualTo(status.value());
        assertThat(response.body().jsonPath().getString("email")).isEqualTo(email);
        assertThat(response.body().jsonPath().getInt("age")).isEqualTo(age);
    }

    /**
     * Scenario : 나의 정보를 관리한다.
     * Given    : 회원을 생성하고
     * <내 정보 조회>
     * Given    : 토큰 인증을하고
     * When     : 토큰을 바탕으로 내 정보 조회를 요청하면
     * Then     : 내 정보가 조회된다.
     * <내 정보 수정>
     * Given    : 토큰 인증을하고
     * When     : 토큰을 바탕으로 내 정보 수정을 요청하면
     * Then     : 내 정보가 수정된다.
     * <내 정보 삭제>
     * Given    : 토큰 인증을하고
     * When     : 토큰을 바탕으로 내 정보 삭제를 요청하면
     * Then     : 내 정보가 삭제된다.
     */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // given
        /* 회원 생성 */
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 토큰_인증(EMAIL, PASSWORD);

        /* 내 정보 조회 */
        // when
        ExtractableResponse<Response> 내_정보_조회_response = 내_회원_정보_조회_요청(accessToken);

        // then
        validateMemberResponse(내_정보_조회_response, HttpStatus.OK, EMAIL, AGE);

        /* 내 정보 수정*/
        // when
        ExtractableResponse<Response> 내_정보_수정_response = 내_회원_정보_수정_요청(accessToken, CHANGED_EMAIL, CHANGED_PASSWORD, CHANGED_AGE);

        // then
        validateMemberResponse(내_정보_수정_response, HttpStatus.OK, CHANGED_EMAIL, CHANGED_AGE);

        /* 내 정보 삭제*/
        // when
        ExtractableResponse<Response> 내_정보_삭제_response = 내_회원_정보_삭제_요청(accessToken);

        // then
        assertThat(내_정보_삭제_response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}