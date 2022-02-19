package nextstep.member.acceptance;

import static nextstep.member.MemberSteps.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.AcceptanceTest;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> 회원_생성_응답 = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> 회원_정보_응답 = 회원_정보_조회_요청(회원_생성_응답);
        ExtractableResponse<Response> 회원_수정_응답 = 회원_정보_수정_요청(회원_생성_응답, "new" + EMAIL, "new" + PASSWORD, AGE);
        ExtractableResponse<Response> 회원_삭제_응답 = 회원_삭제_요청(회원_생성_응답);

        // then
        응답_확인(회원_생성_응답, HttpStatus.CREATED);
        회원_정보_조회됨(회원_정보_응답, EMAIL, AGE);
        응답_확인(회원_수정_응답, HttpStatus.OK);
        응답_확인(회원_삭제_응답, HttpStatus.NO_CONTENT);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // when
        ExtractableResponse<Response> 회원_생성_응답 = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        final String 접근_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);
        ExtractableResponse<Response> 회원_정보_응답 = 토큰_기반_내_회원_정보_조회_요청(접근_토큰);
        ExtractableResponse<Response> 회원_수정_응답 = 회원_정보_수정_요청(접근_토큰, "new" + EMAIL, "new" + PASSWORD, AGE);
        ExtractableResponse<Response> 회원_삭제_응답 = 토근_기반_회원_삭제_요청(접근_토큰);

        // then
        응답_확인(회원_생성_응답, HttpStatus.CREATED);
        회원_정보_조회됨(회원_정보_응답, EMAIL, AGE);
        응답_확인(회원_수정_응답, HttpStatus.OK);
        응답_확인(회원_삭제_응답, HttpStatus.NO_CONTENT);
    }
}