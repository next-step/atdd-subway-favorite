package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.member.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    /**
     * when 회원가입 요청을 하면
     * then 회원이 등록된다.
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
     * given 회원 가입을 요청하고
     * when 회원 정보 조회를 요청하면
     * then 회원 가입한 회원의 정보를 조회할 수 있다.
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
     * given 회원 가입을 요청하고
     * when 회원 정보 수정을 요청하면
     * then 회원 정보 수정이 된다.
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
     * given 회원 가입을 요청하고
     * when 회원 삭제 요청하면
     * then 회원이 삭제된다.
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
     * given 회원 가입을 요청하고 로그인 요청을 한 뒤에
     * when 내 정보를 조회하면
     * then 내 정보를 조회할 수 있다.
     */
    @DisplayName("로그인 후, 내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 베어러_인증_로그인_요청_성공(EMAIL, PASSWORD).response().jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> getMyInfoResponse = 베이러_인증으로_내_회원_정보_조회_요청(accessToken);

        // then
        String email = getMyInfoResponse.jsonPath().getString("email");
        Integer age = getMyInfoResponse.jsonPath().getInt("age");
        assertAll(
                () -> assertThat(email).isEqualTo("email@email.com"),
                () -> assertThat(age).isEqualTo(20)
        );
    }

    /**
     * when 인증 없이 내 정보를 조회하면
     * then 내 정보를 조회할 수 없다.
     */
    @DisplayName("로그인 하지 않으면 내 정보를 조회할 수 없다.")
    @Test
    void getMyInfoExceptionNoAuthentication() {
        // when & then
        인증_없이_내_회원_정보_조회_요청();
    }
}