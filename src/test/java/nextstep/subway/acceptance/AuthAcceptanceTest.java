package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.내_회원_정보_삭제_요청;
import static nextstep.subway.acceptance.MemberSteps.내_회원_정보_수정_요청;
import static nextstep.subway.acceptance.MemberSteps.내_회원_정보_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_정보_조회됨;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.dto.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    private ExtractableResponse<Response> response;

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_생성_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        response = 내_회원_정보_조회_요청(EMAIL, PASSWORD);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Bearer Auth 로그인 후 내 정보 조회")
    @Test
    void myInfoWithBearerAuth() {
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        response = 내_회원_정보_조회_요청(accessToken);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Bearer Auth 로그인 후 내 정보를 수정한다.")
    @Test
    void manageMember() {
        String newEmail = "newEmail";
        Integer newAge = 99;
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        response = 내_회원_정보_수정_요청(accessToken, MemberRequest.of(newEmail, PASSWORD, newAge));
        // Then 회원 정보 수정됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when
        response = 내_회원_정보_조회_요청(accessToken);
        // then
        회원_정보_조회됨(response, newEmail, newAge);
    }

    @DisplayName("Bearer Auth 로그인 후 내 정보를 삭제한다.")
    @Test
    void manageMyInfo() {
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // When
        response = 내_회원_정보_삭제_요청(accessToken);
        // Then 회원 삭제됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // when
        response = 내_회원_정보_조회_요청(accessToken);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
