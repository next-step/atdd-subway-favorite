package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.AuthSteps.*;
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



    /**
     * Feature: 회원 정보를 관리한다.
     *
     *   Scenario: 회원 정보를 관리
     *     When 회원 생성을 요청
     *     Then 회원 생성됨
     *     When 회원 정보 조회 요청
     *     Then 회원 정보 조회됨
     *     When 회원 정보 수정 요청
     *     Then 회원 정보 수정됨
     *     When 회원 삭제 요청
     *     Then 회원 삭제됨
     */
    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        //when
        ExtractableResponse<Response> 회원 = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        //then
        회원_생성_됨(회원);

        //when
        ExtractableResponse<Response> 회원_정보_조회_요청 = 회원_정보_조회_요청(회원);

        //then
        회원_정보_조회됨(회원_정보_조회_요청, EMAIL, AGE);

        //given
        String nextEmail = "icraft2170@gmail.com";
        String nextPassword = "icraft2170^@^";
        int nextAge = 30;

        //when
        ExtractableResponse<Response> 회원_정보_수정_요청 = 회원_정보_수정_요청(회원, nextEmail, nextPassword, nextAge);

        //then
        회원_수정_됨(회원_정보_수정_요청);

        //when
        ExtractableResponse<Response> 회원_삭제_요청 = 회원_삭제_요청(회원);
        //then
        회원_삭제_됨(회원_삭제_요청);
    }


    /**
     * Feature: 내 회원 정보를 관리한다.
     *
     *   Scenario: 내 회원 정보를 관리
     *     When 회원 생성을 요청
     *     Then 회원 생성됨
     *     When 로그인 요청
     *     When 내 정보 조회 요청
     *     Then 내 정보 조회됨
     *     When 내 정보 수정 요청
     *     Then 내 정보 수정됨
     *     When 내 정보 삭제 요청
     *     Then 내 정보 삭제됨
     */
    @DisplayName("나의 정보 관리 테스트")
    @Test
    void manageMyInfo() {
        //when
        ExtractableResponse<Response> 회원 = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        //then
        회원_생성_됨(회원);

        //when
        String 로그인_회원_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);

        //when
        ExtractableResponse<Response> 내_회원_정보_반환 = 내_회원_정보_조회_요청(로그인_회원_토큰);

        //then
        내_회원_정보_조회_되어_있음(내_회원_정보_반환);

        //given
        String nextEmail = "icraft2170@gmail.com";
        String nextPassword = "password26";
        int nextAge = 26;

        //when
        ExtractableResponse<Response> 내_정보_수정_반환 = 내_회원_정보_수정_요청(로그인_회원_토큰, nextEmail, nextPassword, nextAge);

        //then
        내_정보_수정_되어_있음(내_정보_수정_반환);

        //when
        ExtractableResponse<Response> 내_정보_삭제_반환 = 내_회원_정보_삭제_요청(로그인_회원_토큰);

        //then
        내_정보_삭제_되어_있음(내_정보_삭제_반환);
    }


    @DisplayName("인가 없이 내 정보 조회")
    @Test
    void show_me_unauthorized() {
        //when
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청_인가_없이();
        //then
        인가_되지_않음(response);
    }

    @DisplayName("인가 없이 내 정보 수정")
    @Test
    void modify_me_unauthorized() {
        //given
        String email = "email@email.com";
        String password = "password";
        int age = 30;
        //when
        ExtractableResponse<Response> response = 내_회원_정보_수정_요청_인가_없이(email, password, age);
        //then
        인가_되지_않음(response);

    }

    @DisplayName("인가 없이 내 정보 삭제")
    @Test
    void delete_me_unauthorized() {
        //when
        ExtractableResponse<Response> response = 내_회원_정보_삭제_요청_인가_없이();
        //then
        인가_되지_않음(response);

    }
}