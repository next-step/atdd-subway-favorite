package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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

    @DisplayName("내 정보를 조회 한다")
    @Test
    void findMemberOfMine() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String token = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(token);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("내 정보를 수정 한다")
    @Test
    void updateMemberOfMine() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String token = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 내_회원_정보_수정_요청(token, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        회원_수정됨(response);
    }

    @DisplayName("내 정보를 삭제 한다")
    @Test
    void deleteMemberOfMine() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String token = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 내_회원_정보_삭제_요청(token);

        // then
        회원_삭제됨(response);
    }

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> 회원_생성_응답 = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(회원_생성_응답);

        // when
        ExtractableResponse<Response> 회원_조회_응답 = 회원_정보_조회_요청(회원_생성_응답);
        // then
        회원_정보_조회됨(회원_조회_응답, EMAIL, AGE);

        // when
        ExtractableResponse<Response> 회원_수정_응답 = 회원_정보_수정_요청(회원_생성_응답, "new" + EMAIL, "new" + PASSWORD, AGE);
        // then
        회원_수정됨(회원_수정_응답);

        // when
        ExtractableResponse<Response> 회원_삭제_응답 = 회원_삭제_요청(회원_생성_응답);
        // then
        회원_삭제됨(회원_삭제_응답);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // when
        ExtractableResponse<Response> 회원_생성_응답 = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(회원_생성_응답);

        // when
        String 토큰 = 로그인_되어_있음(EMAIL, PASSWORD);
        // then
        토큰_확인(토큰);

        // when
        ExtractableResponse<Response> 내정보 = 내_회원_정보_조회_요청(EMAIL, PASSWORD);
        // then
        회원_정보_조회됨(내정보, EMAIL, AGE);

        // when
        ExtractableResponse<Response> 회원_수정_응답 = 내_회원_정보_수정_요청(토큰, "new" + EMAIL, "new" + PASSWORD, AGE);
        // then
        회원_수정됨(회원_수정_응답);

        // when
        ExtractableResponse<Response> 회원_삭제_응답 = 내_회원_정보_삭제_요청(토큰);
        // then
        회원_삭제됨(회원_삭제_응답);
    }

}