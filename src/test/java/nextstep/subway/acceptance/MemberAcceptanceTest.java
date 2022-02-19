package nextstep.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    /**
     * When 회원 생성을 요청
     * Then 회원 생성됨
     * When 회원 정보 조회 요청
     * Then 회원 정보 조회됨
     * When 회원 정보 수정 요청
     * Then 회원 정보 수정됨
     * When 회원 삭제 요청
     * Then 회원 삭제됨
     */
    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        회원_생성요청_후_응답_검증(EMAIL, PASSWORD, AGE);
        회원_조회요청_후_응답_검증(EMAIL, PASSWORD, AGE);
        회원_수정요청_후_응답_검증(EMAIL, PASSWORD, AGE);
        회원_삭제요청_후_응답검증(EMAIL, PASSWORD, AGE);
    }

    /**
     * When 나의 회원 생성을 요청
     * Then 나의 회원 생성됨
     * When 나의 회원 정보 조회 요청
     * When 나의 회원 정보로 세션 로그인 요청
     * Then 나의 회원 정보로 세션 로그인 되어 있음
     * Then 나의 회원 정보 조회됨
     * When 나의 회원 정보로 로그인 토큰 발행 요청
     * Then 나의 회원 정보로 토큰 발행 받음
     * When 토큰으로 나의 회원 정보 조회 요청
     * Then 토큰으로 나의 회원 정보 조회됨
     * When 토큰으로 나의 회원 정보 수정 요청
     * Then 토큰으로 나의 회원 정보 수정됨
     * When 토큰으로 나의 회원 정보 삭제 요청
     * Then 토큰으로 나의 회원 정보 삭제됨
     */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        String myEmail = "my@my.com";
        String myPassword = "1234";
        int myAge = 100;
        회원_생성요청_후_응답_검증(myEmail, myPassword, myAge);

        내_회원_정보_조회_요청(myEmail, myPassword);

        String accessToken = 로그인_되어_있음(myEmail, myPassword);
        내_회원_정보_조회_요청(accessToken);

        내_회원_정보_수정_요청(accessToken, myEmail, myPassword, myAge + 1);
        int updateAge = 내_회원_정보_조회_요청(accessToken).jsonPath().getInt("age");
        assertThat(updateAge).isEqualTo(myAge + 1);

        내_회원_정보_삭제_요청(accessToken);

        내_회원_정보_조회_요청_권한_없음_실패(accessToken);
    }
}
