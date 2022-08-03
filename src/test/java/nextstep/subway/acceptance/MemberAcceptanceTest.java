package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.AuthSteps.베어러_인증으로_내_회원_정보_조회_요청;
import static nextstep.subway.acceptance.AuthSteps.베어러_토큰_인증으로_내_회원_정보_삭제_요청;
import static nextstep.subway.acceptance.AuthSteps.베어러_토큰_인증으로_내_회원_정보_수정;
import static nextstep.subway.acceptance.AuthSteps.폼_로그인_후_내_회원_정보_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private String 인증_토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();
        인증_토큰 = 로그인_되어_있음("admin@email.com", "password");
    }

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

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        ExtractableResponse<Response> 생성_요청 = 회원_생성_요청("nextstep@naver.com", "1234", 20);

        ExtractableResponse<Response> 수정_요청 = 회원_정보_수정_요청(생성_요청, "nextstep@google.com", "google", 30);
        assertThat(수정_요청.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> 삭제_요청 = 회원_삭제_요청(생성_요청);
        assertThat(삭제_요청.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        ExtractableResponse<Response> 회원_정보 = 베어러_인증으로_내_회원_정보_조회_요청(인증_토큰);
        회원_정보_조회됨(회원_정보, EMAIL, AGE);

        ExtractableResponse<Response> 수정_요청 = 베어러_토큰_인증으로_내_회원_정보_수정(인증_토큰, EMAIL, PASSWORD, AGE);
        assertThat(수정_요청.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> 삭제_요청 = 베어러_토큰_인증으로_내_회원_정보_삭제_요청(인증_토큰);
        assertThat(삭제_요청.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}