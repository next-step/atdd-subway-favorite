package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;

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

    @Nested
    @DisplayName("회원 정보")
    class members {
        ExtractableResponse<Response> createResponse;

        @BeforeEach
        void setUp() {
            // given
            createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        }

        @DisplayName("회원 정보를 조회한다.")
        @Test
        void getMember() {
            // when
            ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);

            // then
            회원_정보_조회됨(response, EMAIL, AGE);

        }

        @DisplayName("회원 정보를 수정한다.")
        @Test
        void updateMember() {
            // when
            ExtractableResponse<Response> response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }

        @DisplayName("회원 정보를 삭제한다.")
        @Test
        void deleteMember() {
            // when
            ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }
    }


    @Nested
    @DisplayName("내 회원 정보 조회")
    class MyInfo {
        ExtractableResponse<Response> createResponse;

        @BeforeEach
        void setUp() {
            // given
            createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        }

        @Test
        @DisplayName("토큰으로 회원 정보를 조회한다.")
        void getMyInfoByToken() {
            //when 토큰으로 내 회원 정보를 조회한다.
            String accessToken = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
            ExtractableResponse<Response> response = 토큰으로_내_회원_정보_조회_요청(accessToken);
            //then
            Assertions.assertAll(
                    () -> assertThat(response.jsonPath().getString("email")).isEqualTo(EMAIL),
                    () -> assertThat(response.jsonPath().getInt("age")).isEqualTo(AGE)
            );
        }

        @Test
        @DisplayName("잘못된 토큰으로 회원 정보를 조회한다.")
        void getMyInfoByNoneToken() {
            //when 토큰없이 내 회원 정보를 조회한다.
            ExtractableResponse<Response> response = 토큰으로_내_회원_정보_조회_요청("0000");
            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        }

    }
}
