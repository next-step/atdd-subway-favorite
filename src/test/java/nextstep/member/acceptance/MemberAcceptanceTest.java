package nextstep.member.acceptance;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.member.acceptance.MemberSteps.*;
import static nextstep.study.AuthSteps.로그인_후_엑세스토큰_획득;
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

    /**
     * When 없는 회원의 정보를 조회하면
     * Then 에러를 반환한다.
     */
    @DisplayName("없는 회원 정보를 조회한다.")
    @Test
    void getMemberThrowException() {
        // when
        var response = 회원_정보_조회_요청(1L);

        // then
        에러코드_검증(response);
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

    /**
     * When 없는 회원의 정보를 조회하면
     * Then 에러를 반환한다.
     */
    @DisplayName("없는 회원 정보를 수정한다.")
    @Test
    void updateMemberThrowException() {
        // when
        var response = 회원_정보_수정_요청(1L, " test", "test", 1);

        // then
        에러코드_검증(response);
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
        //given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_후_엑세스토큰_획득(EMAIL, PASSWORD);

        //when
        var myInfoResponse = 내_정보_조회(accessToken);

        //then
        assertThat(myInfoResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        회원_정보_조회됨(myInfoResponse, EMAIL, AGE);
    }

    /**
     * When 잘못된 토큰을 전달하면
     * Then 에러를 반환한다.
     */
    @DisplayName("회원 정보 요청에 잘못된 토큰을 전달하면 에러를 리턴한다.")
    @Test
    void getMyInfoThrowException() {
        // when
        var response = 내_정보_조회("");

        // then
        에러코드_검증(response, HttpStatus.UNAUTHORIZED);
    }
}