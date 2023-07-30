package nextstep.api.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import static nextstep.api.auth.acceptance.AuthSteps.github_로그인_요청;
import static nextstep.api.auth.acceptance.AuthSteps.일반_로그인_요청;
import static nextstep.api.auth.oauth2.github.VirtualUsers.사용자1;
import static nextstep.api.auth.oauth2.github.VirtualUsers.사용자2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

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

        private final VirtualUsers registeredUser = 사용자1;
        private final VirtualUsers nonRegisteredUser = 사용자2;

        @BeforeEach
        void setUp() {
            memberRepository.save(registeredUser.toBasicMember());
        }

        @DisplayName("로그인에 성공한다")
        @Test
        void success() {
            final var email = registeredUser.getEmail();
            final var password = registeredUser.getPassword();

            final var response = 일반_로그인_요청(email, password)
                    .statusCode(HttpStatus.OK.value()).extract();

            assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
        }

        @DisplayName("로그인에 실패한다")
        @Nested
        class Fail {

            @Test
            void 이메일이_등록되어_있어야_한다() {
                final var email = nonRegisteredUser.getEmail();
                final var password = registeredUser.getPassword();

                일반_로그인_요청(email, password).statusCode(HttpStatus.UNAUTHORIZED.value());
            }

            @Test
            void 비밀번호가_일치해야_한다() {
                final var email = registeredUser.getEmail();
                final var password = nonRegisteredUser.getPassword();

                일반_로그인_요청(email, password).statusCode(HttpStatus.UNAUTHORIZED.value());
            }
        }

        @DisplayName("Github 연동으로 로그인한다")
        @Nested
        class GithubAuth {

            @DisplayName("로그인에 성공한다")
            @Test
            void success() {
                final var code = 사용자1.getCode();

                final var response = github_로그인_요청(code)
                        .statusCode(HttpStatus.OK.value()).extract();

                assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
            }

            @DisplayName("로그인에 실패한다")
            @Nested
            class Fail {

                @Test
                void Github_등록된_계정이어야_한다() {
                    final var code = "non_registered_user_code";

                    github_로그인_요청(code).statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
            }
        }
    }
}
