package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.*;


class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(EMAIL, PASSWORD);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    /**
        When 회원 생성을 요청
        Then 회원 생성됨
        When 회원 정보 조회 요청
        Then 회원 정보 조회됨
        When 회원 정보 수정 요청
        Then 회원 정보 수정됨
        When 회원 삭제 요청
        Then 회원 삭제됨
     */
    @DisplayName("회원 정보를 관리")
    @Test
    void 회원_생명_주기(){
        //when
        ExtractableResponse<Response> 회원_생성_응답 = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        //then
        상태_값_검사(회원_생성_응답, HttpStatus.CREATED);
        //when
        ExtractableResponse<Response> 내_회원_정보_조회_응답 = 내_회원_정보_조회_요청(EMAIL, PASSWORD);
        //then
        회원_정보_조회됨(내_회원_정보_조회_응답, EMAIL, AGE);
        //when
        String updateEmail = "sss@naver.com";
        String updatePassword = "updatePw";
        int updateAge =3;
        회원_정보_수정_요청(회원_생성_응답,updateEmail,updatePassword,updateAge);
        //then
        ExtractableResponse<Response> 내_회원_정보_수정_조회_응답 = 내_회원_정보_조회_요청(updateEmail, updatePassword);
        회원_정보_조회됨(내_회원_정보_수정_조회_응답, updateEmail, updateAge);
        //when
        ExtractableResponse<Response> 회원_삭제_응답 = 회원_삭제_요청(회원_생성_응답);
        //then
        상태_값_검사(회원_삭제_응답, HttpStatus.NO_CONTENT);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(accessToken);

        회원_정보_조회됨(response, EMAIL, AGE);
    }
}
