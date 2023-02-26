package nextstep.subway.acceptance.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.member.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.member.MemberSteps.토큰_인증으로_내_회원_정보_조회_요청;
import static nextstep.subway.acceptance.member.MemberSteps.회원_삭제_요청;
import static nextstep.subway.acceptance.member.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.member.MemberSteps.회원_정보_수정_요청;
import static nextstep.subway.acceptance.member.MemberSteps.회원_정보_조회_요청;
import static nextstep.subway.acceptance.member.MemberSteps.회원_정보_조회됨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("회원 관리 기능")
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
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String token = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        var response = 토큰_인증으로_내_회원_정보_조회_요청(token);

        assertAll(() -> {
            assertThat(response.jsonPath().getString("email")).isEqualTo(EMAIL);
            assertThat(response.jsonPath().getLong("age")).isEqualTo(AGE);
        });
    }

    @DisplayName("토큰값이 올바르지 않으면 내 정보를 조회할 수 없다")
    @Test
    void getMyInfoException() {
        var response = 토큰_인증으로_내_회원_정보_조회_요청("abcd1234");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
