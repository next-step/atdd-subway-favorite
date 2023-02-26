package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AcceptanceTest;
import nextstep.utils.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static nextstep.member.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.member.acceptance.MemberSteps.회원_삭제_요청;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.member.acceptance.MemberSteps.회원_정보_수정_요청;
import static nextstep.member.acceptance.MemberSteps.회원_정보_조회_요청;
import static nextstep.member.acceptance.MemberSteps.회원_정보_조회됨;
import static nextstep.utils.DataLoader.ADMIN_AGE;
import static nextstep.utils.DataLoader.ADMIN_EMAIL;
import static nextstep.utils.DataLoader.ADMIN_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemberAcceptanceTest extends AcceptanceTest {

    @Autowired
    private DataLoader dataLoader;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        dataLoader.loadData();
    }

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        ExtractableResponse<Response> response = 회원_생성_요청(ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_AGE);

        // when
        ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회됨(response, ADMIN_EMAIL, ADMIN_AGE);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_AGE);

        // when
        ExtractableResponse<Response> response = 회원_정보_수정_요청(createResponse, "new" + ADMIN_EMAIL, "new" + ADMIN_PASSWORD, ADMIN_AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_AGE);

        // when
        ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        // Given
        String accessToken = 베어러_인증_로그인_요청(ADMIN_EMAIL, ADMIN_PASSWORD).jsonPath().getString("accessToken");

        // When
        ExtractableResponse<Response> response = 회원_정보_조회_요청(accessToken);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertAll(
                () -> assertThat(response.jsonPath().getString("id")).isNotNull(),
                () -> assertThat(response.jsonPath().getString("email")).isEqualTo(ADMIN_EMAIL),
                () -> assertThat(response.jsonPath().getInt("age")).isEqualTo(ADMIN_AGE)
        );
    }

    @DisplayName("인증받지 않았다면 내 정보를 조회할 수 없다.")
    @Test
    void getMyInfoWhenUnauthentication() {
        // When
        ExtractableResponse<Response> response = 회원_정보_조회_요청("invalid token");

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}