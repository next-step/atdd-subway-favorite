package nextstep.member.acceptance;

import static nextstep.member.acceptance.AuthAcceptanceSteps.베어러_인증_로그인_요청;
import static nextstep.member.acceptance.MemberSteps.베어러_인증으로_내_회원_정보_조회_요청;
import static nextstep.member.acceptance.MemberSteps.유효하지_않은_토큰으로_내_정보를_조회시_예외_처리한다;
import static nextstep.member.acceptance.MemberSteps.회원_삭제_요청;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.member.acceptance.MemberSteps.회원_정보_수정_요청;
import static nextstep.member.acceptance.MemberSteps.회원_정보_조회_요청;
import static nextstep.member.acceptance.MemberSteps.회원_정보_조회됨;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("멤버 관련 기능")
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

    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String token = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

        // when
        var response = 베어러_인증으로_내_회원_정보_조회_요청(token);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);
    }

    /**
     * when 유효하지 않은 토큰으로 내 정보를 조회시
     * then 예외 처리한다.
     */
    @DisplayName("유효하지 않은 토큰으로 내 정보를 조회시 예외 처리한다.")
    @Test
    void getMyInfoUnAuthorizedException() {
        // when & then
        유효하지_않은_토큰으로_내_정보를_조회시_예외_처리한다("유효하지 않은 토큰");
    }
}
