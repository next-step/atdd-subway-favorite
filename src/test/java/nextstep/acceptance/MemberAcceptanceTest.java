package nextstep.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.acceptance.support.AcceptanceTest;
import nextstep.fixture.AuthFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.acceptance.support.MemberSteps.내_정보_조회가_성공한다;
import static nextstep.acceptance.support.MemberSteps.베어러_인증으로_내_회원_정보_조회_요청;
import static nextstep.acceptance.support.MemberSteps.회원_삭제_요청;
import static nextstep.acceptance.support.MemberSteps.회원_생성_요청;
import static nextstep.acceptance.support.MemberSteps.회원_정보_수정_요청;
import static nextstep.acceptance.support.MemberSteps.회원_정보_조회_요청;
import static nextstep.acceptance.support.MemberSteps.회원_정보_조회됨;
import static nextstep.fixture.AuthFixture.알렉스;
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

    @Nested
    @DisplayName("AccessToken을 Header에 포함하여 내 정보 조회를 요청하면")
    class Context_with_select_my_info_with_access_token {

        private final AuthFixture 인증_주체 = 알렉스;

        @Test
        @DisplayName("인증 주체의 회원 정보가 반환된다")
        void it_returns_member() throws Exception {
            ExtractableResponse<Response> 내_정보_조회_결과 = 베어러_인증으로_내_회원_정보_조회_요청(인증_주체);

            내_정보_조회가_성공한다(내_정보_조회_결과);
            회원_정보_조회됨(내_정보_조회_결과, 인증_주체);
        }
    }
}
