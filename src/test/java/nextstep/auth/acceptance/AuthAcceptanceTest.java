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

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        // given
        유저를_가입시킨다(properUser);

        // when
        String accessToken = 토큰_로그인_요청_성공(properUser.getEmail(), properUser.getPassword());

        // then
        assertThat(accessToken).isNotBlank();
    }

    @DisplayName("깃허브 로그인 요청")
    @Nested
    class GithubLogin {
        @Test
        void success() {
            // when
            String accessToken = 깃허브_로그인_요청_성공(properUser.getCode());

            // then
            assertThat(accessToken).isNotBlank();
        }

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