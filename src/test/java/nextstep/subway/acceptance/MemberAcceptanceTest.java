package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Feature : 회원 정보를 관리한다.
 */
class MemberAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String CHANGED_EMAIL = "test@email.com";
    private static final String PASSWORD = "password";
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

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
    }
}