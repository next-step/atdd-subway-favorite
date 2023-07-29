package nextstep.api.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.api.auth.oauth2.github.VirtualUsers;
import nextstep.api.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;

@DisplayName("로그인 관련 기능")
class AuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("이메일과 비밀번호로 로그인한다")
    @Nested
    class BearerAuth {

        private final VirtualUsers registeredUser = VirtualUsers.사용자1;
        private final VirtualUsers nonRegisteredUser = VirtualUsers.사용자2;

        @BeforeEach
        void setUp() {
            memberRepository.save(registeredUser.toBasicMember());
        }

        @DisplayName("로그인에 성공한다")
        @Test
        void success() {
            final var params = Map.of(
                    "email", registeredUser.getEmail(),
                    "password", registeredUser.getPassword()
            );

            ExtractableResponse<Response> response = RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(params)
                    .when().post("/login/token")
                    .then()
                    .statusCode(HttpStatus.OK.value()).extract();

            assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
        }

        @DisplayName("로그인에 실패한다")
        @Nested
        class Fail {

            @Test
            void 이메일이_등록되어_있어야_한다() {
                final var params = Map.of(
                        "email", nonRegisteredUser.getEmail(),
                        "password", registeredUser.getPassword()
                );

                ExtractableResponse<Response> response = RestAssured.given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(params)
                        .when().post("/login/token")
                        .then()
                        .statusCode(HttpStatus.UNAUTHORIZED.value()).extract();
            }

            @Test
            void 비밀번호가_일치해야_한다() {

                final var params = Map.of(
                        "email", registeredUser.getEmail(),
                        "password", nonRegisteredUser.getPassword()
                );

                ExtractableResponse<Response> response = RestAssured.given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(params)
                        .when().post("/login/token")
                        .then()
                        .statusCode(HttpStatus.UNAUTHORIZED.value()).extract();
            }
        }
    }

    @DisplayName("Github 연동으로 로그인한다")
    @Nested
    class GithubAuth {

        @DisplayName("로그인에 성공한다")
        @Test
        void success() {
            final var params = Map.of("code", VirtualUsers.사용자1.getCode());

            ExtractableResponse<Response> response = RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(params)
                    .when().post("/login/github")
                    .then()
                    .statusCode(HttpStatus.OK.value()).extract();

            assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
        }

        @DisplayName("로그인에 실패한다")
        @Nested
        class Fail {

            @Test
            void Github_등록된_계정이어야_한다() {
                final var params = Map.of("code", "non_registered_user_code");

                ExtractableResponse<Response> response = RestAssured.given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(params)
                        .when().post("/login/github")
                        .then()
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).extract();
            }
        }
    }
}
