package nextstep.subway.member.acceptance;

import nextstep.subway.auth.application.provider.TokenType;
import nextstep.subway.testhelper.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.testhelper.apicaller.MemberApiCaller.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String SECOND_EMAIL = "email2@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        var response = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var response = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        var accessToken = 회원_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

        // when
        var response = 회원_정보_수정_요청(createResponse, accessToken, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("본인이 아닌 회원 정보를 수정하면 실패한다.")
    @Test
    void updateNotYourself() {
        // given
        var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        회원_생성_요청(SECOND_EMAIL, PASSWORD, AGE);
        var accessToken = 회원_로그인_요청(SECOND_EMAIL, PASSWORD).jsonPath().getString("accessToken");

        // when
        var response = 회원_정보_수정_요청(createResponse, accessToken, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        var accessToken = 회원_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

        // when
        var response = 회원_삭제_요청(createResponse, accessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("본인이 아닌 회원 정보를 삭제하면 실패한다.")
    @Test
    void deleteNotYourself() {
        // given
        var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        회원_생성_요청(SECOND_EMAIL, PASSWORD, AGE);
        var accessToken = 회원_로그인_요청(SECOND_EMAIL, PASSWORD).jsonPath().getString("accessToken");

        // when
        var response = 회원_삭제_요청(createResponse, accessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * When 토큰을 통해 내 정보를 조회하면
     * Then 내 정보를 조회할 수 있다
     */
    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        // given
        var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // and
        var accessToken = 회원_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

        // when
        var response = 내_정보_조회_요청(accessToken, TokenType.JWT);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);
    }
}
