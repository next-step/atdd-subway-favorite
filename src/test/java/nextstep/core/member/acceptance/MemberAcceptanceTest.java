package nextstep.core.member.acceptance;

import nextstep.common.annotation.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.core.member.fixture.MemberFixture.*;
import static nextstep.core.auth.step.AuthSteps.성공하는_토큰_발급_요청;
import static nextstep.core.member.step.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest
class MemberAcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        var response = 회원_생성_요청(브라운);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        var createResponse = 회원_생성_요청(브라운);

        // when
        var response = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_확인(response, 브라운.이메일, 브라운.나이);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        var createResponse = 회원_생성_요청(브라운);

        // when
        var response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        var createResponse = 회원_생성_요청(브라운);

        // when
        var response = 회원_삭제_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Nested
    class 내_정보_조회 {

        @Nested
        class 성공 {
            /**
             * Given 회원을 생성한다.
             * Given 생성된 회원 정보를 통해 토큰을 발급받는다.
             * When  토큰을 통해 내 정보를 조회하면
             * Then  내 정보를 조회할 수 있다
             */
            @Test
            void 정상적인_토큰으로_조회() {
                // given
                회원_생성_요청(스미스);

                var 발급된_토큰 = 성공하는_토큰_발급_요청(스미스);

                // when
                var 토큰으로_전달받은_회원_정보 = 성공하는_토큰으로_회원정보_요청(발급된_토큰);

                // then
                회원_정보_확인(토큰으로_전달받은_회원_정보, 스미스.이메일, 스미스.나이);
            }

        }

        @Nested
        class 실패 {

            @BeforeEach
            void 사전_회원_생성() {
                회원_생성_요청(존슨);
            }

            /**
             * Given 회원을 생성하고, 저장소에 저장한다.
             * Given 생성된 회원 정보를 통해 토큰을 발급받는다.
             * When  비어있는 토큰으로 내 정보를 조회하면
             * Then  내 정보를 조회할 수 없다.
             */
            @Test
            void 비어있는_토큰을_전달한_경우() {
                // given
                var 비어있는_토큰 = "";

                // when
                실패하는_토큰으로_회원정보_요청(비어있는_토큰);
            }

            /**
             * Given 회원을 생성하고, 저장소에 저장한다.
             * Given 생성된 회원 정보를 통해 토큰을 발급받는다.
             * When  발급받은 토큰을 임의로 수정하고 내 정보를 조회하면
             * Then  내 정보를 조회할 수 없다.
             */
            @Test
            void 비정상적인_토큰을_전달한_경우() {
                // given
                회원_생성_요청(윌리엄스);

                var 임의로_수정된_토큰 = "Changed" + 성공하는_토큰_발급_요청(윌리엄스);

                // when
                실패하는_토큰으로_회원정보_요청(임의로_수정된_토큰);
            }
        }
    }
}