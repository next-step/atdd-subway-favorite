package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.common.constants.ErrorConstant.INVALID_AUTHENTICATION_INFO;
import static nextstep.member.acceptance.AuthSteps.권한없는_요청_검증;
import static nextstep.member.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    /**
     * When 회원 생성을 요청하면
     * Then 회원이 생성된다.
     */
    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        ExtractableResponse<Response> response = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 회원을 생성하고
     * When 회원 정보를 요청하면
     * Then 회원정보가 조회된다.
     */
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

    /**
     * Given 회원을 생성하고
     * When 회원정보 수정을 요청하면
     * Then 회원 정보가 수정된다.
     */
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

    /**
     * Given 회원을 생성하고
     * When 회원 삭제를 요청하면
     * Then 회원이 삭제된다.
     */
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
     * Given 회원을 생성하고
     * When 로그인을 통해 토큰을 발급 받고, 토큰으로 내 정보를 요청하면
     * Then 내 정보가 조회된다.
     */
    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var response = 베어러_인증으로_내_회원_정보_조회_요청(EMAIL, PASSWORD);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);
    }

    /**
     * When 비로그인 상태에서 내 정보 조회를 요청하면
     * Then 예외가 발생한다.
     */
    @Test
    @DisplayName("비로그인 상태에서 내 정보 조회")
    void getMyInfo_notLogin() {
        // when
        var response = 비로그인_내_회원_정보_조회_요청();

        // then
        권한없는_요청_검증(response, INVALID_AUTHENTICATION_INFO);
    }

    /**
     * When 유효하지 않은 토큰으로 내 정보 조회를 요청하면
     * Then 예외가 발생한다.
     */
}