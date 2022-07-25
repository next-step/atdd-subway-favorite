package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.AbstractIntegerAssert;
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
        회원_정보가_수정됨(response);
    }


    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);

        // then
        회원이_삭제됨(response);
    }


    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        var 생성_요청 = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        var 회원_정보_조회_요청 = 회원_정보_조회_요청(생성_요청);
        회원_정보_조회됨(회원_정보_조회_요청, EMAIL, AGE);

        var 수정_요청 = 회원_정보_수정_요청(생성_요청, "new" + EMAIL, "new" + PASSWORD, AGE);
        회원_정보가_수정됨(수정_요청);

        var 삭제_요청 = 회원_삭제_요청(생성_요청);
        회원이_삭제됨(삭제_요청);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        var 인증토큰 = 로그인_되어_있음(EMAIL, PASSWORD);

        var 회원_정보 = 베어러_토큰_인증으로_내_회원_정보_조회_요청(인증토큰);

        회원_정보_조회됨(회원_정보, EMAIL, AGE);

        var 수정_요청 = 베어러_토큰_인증으로_내_회원_정보_수정(인증토큰, EMAIL, PASSWORD, AGE);
        회원_정보가_수정됨(수정_요청);

        베어러_토큰_인증으로_내_회원_정보_삭제_요청(인증토큰);
    }
}