package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.member.MemberSteps.*;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> inquiryResponse = 회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(inquiryResponse, EMAIL, AGE);

        //when
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(updateResponse);

        //when
        ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);
        // then
        회원_삭제됨(response);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        //when
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        //회원 정보 조회 요청
        ExtractableResponse<Response> inquiryResponse = 내_회원_정보_조회_요청(tokenResponse);
        //회원 정보 조회됨
        회원_정보_조회됨(inquiryResponse, EMAIL, AGE);

        //회원 정보 수정 요청
        ExtractableResponse<Response> updateResponse = 내_회원_정보_수정_요청(tokenResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        //회원 정보 수정됨
        내_회원_정보_수정됨(updateResponse);

        //회원 정보 삭제 요청
        ExtractableResponse<Response> deleteResponse = 내_회원_정보_삭제_요청(createResponse);
        //회원 정보 삭제됨
        내_회원_정보_삭제됨(deleteResponse);
    }
}
