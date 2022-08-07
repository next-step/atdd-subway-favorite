package nextstep.subway.acceptance.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.utils.AdministratorInfo.ADMIN_EMAIL;
import static nextstep.subway.utils.AdministratorInfo.ADMIN_PASSWORD;
import static nextstep.subway.acceptance.step.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static nextstep.subway.utils.RestAssuredStep.*;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    private ExtractableResponse<Response> 회원생성_Response;
    @BeforeEach
    public void setUp(){
        super.setUp();

        회원생성_Response = 회원_생성_요청( EMAIL, PASSWORD, AGE);
    }

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        // then
        assertThat(회원생성_Response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given

        // when
        ExtractableResponse<Response> response = 회원_정보_조회_요청(회원생성_Response);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        // when
        ExtractableResponse<Response> response = 회원_정보_수정_요청(관리자토큰, 회원생성_Response, "new" + EMAIL, "new" + PASSWORD, AGE);

        회원_정보_조회_요청(회원생성_Response);
        // then
        검증_STATUS_OK(response);

    }

    @DisplayName("권한 검증 실패로 인한 회원수정 실패..")
    @Test
    void createMemberFail() {
        String invalidToken = "invalidToken";
        // when
        ExtractableResponse<Response> response = 회원_정보_수정_요청(invalidToken, 회원생성_Response, "new" + EMAIL, "new" + PASSWORD, AGE);
        // then
        검증_UNAUTHORIZED(response);
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        관리자토큰 = 로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD);
        // when
        ExtractableResponse<Response> response = 회원_삭제_요청(관리자토큰, 회원생성_Response);

        // then
        검증_NO_CONTENT(response);
    }

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        ExtractableResponse<Response> 조회응답 = 회원_정보_조회_요청(회원생성_Response);
        회원_정보_조회됨(조회응답, EMAIL, AGE);

        ExtractableResponse<Response> 수정응답 = 회원_정보_수정_요청(관리자토큰, 회원생성_Response, EMAIL+"_edit", PASSWORD+"_edit", AGE);
        검증_STATUS_OK(수정응답);

        회원_정보_조회_요청(회원생성_Response);

        ExtractableResponse<Response> 삭제응답 = 회원_삭제_요청(관리자토큰, 회원생성_Response);

        검증_NO_CONTENT(삭제응답);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        String 멤버토큰 = 로그인_되어_있음(EMAIL, PASSWORD);
        ExtractableResponse<Response> 수정응답 = 베어러_인증_내_회원_정보_수정(멤버토큰,"edit", "1", 12);
        검증_STATUS_OK(수정응답);
        멤버토큰 = 로그인_되어_있음("edit", "1");
        ExtractableResponse<Response> 삭제응답 = 베어러_인증_내_회원_정보_삭제_요청(멤버토큰);
        검증_NO_CONTENT(삭제응답);
    }
}