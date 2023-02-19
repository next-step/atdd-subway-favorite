package nextstep.subway.acceptance;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.dto.MemberErrorResponse;
import nextstep.member.application.exception.MemberErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @BeforeEach
    public void setUp() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);
    }

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

    // Given 회원가입 요청을하고
    // Given 토큰생성요청을 하고
    // When 해당 토큰으로 회원정보를 조회하면
    // Then 회원정보를 조회할 수 있다
    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        // given
        String accessToken = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> response = 베어러_인증토큰으로_내_회원_정보_조회_요청(accessToken);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getString("email")).isEqualTo(EMAIL)
        );
    }

    @DisplayName("내 정보 조회시 회원등록이되지않았다면 예외가 발생한다")
    @Test
    void 내_정보_조회시_회원등록이되지않았다면_예외가_발생한다() {
        // given
        String notRegisterMemberEmail = "notRegister@email.com";
        String notRegisterMemberPassword = "notregisterpassword123";

        // when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(notRegisterMemberEmail, notRegisterMemberPassword);

        MemberErrorResponse memberErrorResponse = response.as(new TypeRef<>() {
        });

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(memberErrorResponse.getCode()).isEqualTo(MemberErrorCode.NOT_FOUND_MEMBER.getCode())
        );
    }

    @DisplayName("내 정보 조회시 accessToken이 잘못된토큰이라면 예외가 발생한다")
    @Test
    void 내_정보_조회시_accessToken이_잘못된토큰이라면_예외가_발생한다() {
        // given
        String accessToken = "e2=f3fdfds=wrongAccessToken";

        // when
        ExtractableResponse<Response> response = 베어러_인증토큰으로_내_회원_정보_조회_요청(accessToken);

        MemberErrorResponse memberErrorResponse = response.as(new TypeRef<>() {
        });
        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(memberErrorResponse.getCode()).isEqualTo(MemberErrorCode.INVALID_TOKEN.getCode())
        );
    }
}
