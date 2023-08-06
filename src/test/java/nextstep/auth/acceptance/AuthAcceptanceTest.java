package nextstep.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import nextstep.auth.util.VirtualUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static nextstep.auth.acceptance.AuthSteps.*;
import static nextstep.common.CommonSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("이메일과 비밀번호로 로그인 요청")
    @Nested
    class BearerAuthLogin {
        @DisplayName("로그인에 성공한다.")
        @Test
        void success() {
            // given
            유저를_가입시킨다(properUser);

            // when
            String accessToken = 토큰_로그인_요청_성공(properUser.getEmail(), properUser.getPassword());

            // then
            assertThat(accessToken).isNotBlank();
        }

        @DisplayName("로그인에 실패한다.")
        @Nested
        class Fail {
            @DisplayName("아이디가 존재하지 않는다")
            @Test
            void invalidUsername() {
                // when
                ExtractableResponse<Response> response = 토큰_로그인_요청("invalid_email@gmail.com", "not_used_password");

                // then
                checkHttpResponseCode(response, HttpStatus.UNAUTHORIZED);
            }

            @DisplayName("틀린 비밀번호입니다.")
            @Test
            void passwordMismatch() {
                // when
                ExtractableResponse<Response> response = 토큰_로그인_요청(properUser.getEmail(), "wrong_password");

                // then
                checkHttpResponseCode(response, HttpStatus.UNAUTHORIZED);
            }

        }
    }


    @DisplayName("깃허브 연동하여 로그인 요청")
    @Nested
    class GithubAuthLogin {
        @DisplayName("로그인에 성공한다.")
        @Test
        void success() {
            // when
            String accessToken = 깃허브_로그인_요청_성공(properUser.getCode());

            // then
            assertThat(accessToken).isNotBlank();
        }

        @DisplayName("로그인에 실패한다.")
        @Test
        void fail() {
            // when
            ExtractableResponse<Response> response = 깃허브_로그인_요청("wrong_code");

            // then
            checkHttpResponseCode(response, HttpStatus.UNAUTHORIZED);
        }
    }

    private void 유저를_가입시킨다(VirtualUser properUser) {
        memberRepository.save(new Member(properUser.getEmail(), properUser.getPassword(), properUser.getAge()));
    }
}