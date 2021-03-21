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
        // When 회원 생성을 요청
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // Then 회원 생성됨
        회원_생성됨(createResponse);

        // When 회원 정보 조회 요청
        ExtractableResponse<Response> searchResponse = 회원_정보_조회_요청(createResponse);

        // Then 회원 정보 조회됨
        회원_정보_조회됨(searchResponse, EMAIL, AGE);

        // When 회원 정보 수정 요청
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

        // Then 회원 정보 수정됨
        회원_정보_수정됨(updateResponse);

        // When 회원 삭제 요청
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);

        // Then 회원 삭제됨
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        //When
        ExtractableResponse<Response> 회원_생성_응답  = 회원_생성_요청(EMAIL,PASSWORD,AGE);

        //Then
        회원_생성됨(회원_생성_응답);

        //When
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(EMAIL, PASSWORD);

        //Then
        TokenResponse tokenResponse = 로그인_성공함(로그인_응답);

        //When
        ExtractableResponse<Response> 내_회원_조회_응답 = 내_회원_정보_조회_요청(tokenResponse);

        //Then
        회원_정보_조회됨(내_회원_조회_응답,EMAIL,AGE);

        //When
        ExtractableResponse<Response> 잘못된_토큰으로_정보_조회_응답 = 잘못된_토큰으로_정보_조회_요청();

        //Then
        응답_실패됨(잘못된_토큰으로_정보_조회_응답);

        //When
        ExtractableResponse<Response> 토큰없이_정보_조회_응답 = 토큰없이_정보_조회_요청();

        //Then
        응답_실패됨(토큰없이_정보_조회_응답);

        //When
        ExtractableResponse<Response> 내_회원_수정_응답 = 내_정보_수정_요청(tokenResponse,NEW_EMAIL,NEW_PASSWORD,NEW_AGE);

        //Then
        회원_정보_수정됨(내_회원_수정_응답);

        //When
        ExtractableResponse<Response>  내_회원_삭제_응답 = 내_정보_삭제_요청(tokenResponse);

        //Then
        회원_삭제됨(내_회원_삭제_응답);
    }
}