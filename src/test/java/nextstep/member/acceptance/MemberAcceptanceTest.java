package nextstep.member.acceptance;

import nextstep.member.application.dto.MemberResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.auth.token.acceptance.TokenSteps.로그인_요청;
import static nextstep.member.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
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

        // when
        var response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var response = 회원_삭제_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
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
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        var 토큰 = 로그인_요청(EMAIL, PASSWORD);

        var 내정보 = 내정보_조회_요청(토큰);

        회원_정보_일치_확인(내정보, EMAIL, AGE);
    }

    /**
     * Given 회원 가입을 생성하고
     * And 토큰이 만료된 상태에서
     * When 토큰을 통해 내 정보를 조회하면
     * Then 401 에러를 반환한다
     */
    @DisplayName("토큰 만료시 401 에러를 반환한다")
    @Test
    void getMyInfo_fail_tokenOver() throws InterruptedException {
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        var 토큰 = 로그인_요청(EMAIL, PASSWORD);
        Thread.sleep(1000);

        var 상태코드 = 내정보_조회_요청_상태코드_반환(토큰);

        상태코드_권한_없음(상태코드);
    }
}