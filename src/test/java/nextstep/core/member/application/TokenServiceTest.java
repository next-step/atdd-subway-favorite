package nextstep.core.member.application;

import nextstep.common.annotation.ApplicationTest;
import nextstep.core.member.application.dto.MemberRequest;
import nextstep.core.member.application.dto.TokenResponse;
import nextstep.core.member.exception.NotFoundMemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("토큰 서비스 레이어 테스트")
@ApplicationTest
public class TokenServiceTest {

    TokenService tokenService;

    @Autowired
    MemberService memberService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void 사전_토큰_서비스_생성() {
        tokenService = new TokenService(memberService, jwtTokenProvider);
    }

    @Nested
    class 토큰_발급 {
        @Nested
        class 성공 {
            /**
             * Given 회원을 생성한다.
             * When  생성된 회원 정보로 토큰 발급을 요청할 경우
             * Then  정상적으로 토큰이 발급된다.
             */
            @Test
            void 저장된_회원으로_토큰_발급_요청() {
                // given
                String email = "admin@email.com";
                String password = "password";
                int age = 20;

                MemberRequest memberRequest = new MemberRequest(email, password, age);
                memberService.createMember(memberRequest);

                // when
                TokenResponse tokenResponse = tokenService.createToken(email, password);

                // then
                assertThat(tokenResponse.getAccessToken()).isNotBlank();
            }
        }

        @Nested
        class 실패 {
            /**
             * Given 회원을 생성한다.
             * When  생성된 회원의 비밀번호가 아닌 회원 정보로 토큰 발급을 요청할 경우
             * Then  토큰이 발급되지 않는다.
             */
            @Test
            void 비밀번호가_다른_회원의_토큰_발급_요청() {
                // given
                String email = "admin@email.com";
                String password = "password";
                String changedPassword = "changed password";
                int age = 20;

                MemberRequest memberRequest = new MemberRequest(email, password, age);
                memberService.createMember(memberRequest);

                // when, then
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                            tokenService.createToken(email, changedPassword);
                        }).withMessageMatching("비밀번호가 다릅니다.");
            }

            /**
             * Given 저장소에 저장하지 않는 회원을 생성한다.
             * When  회원 저장소에 존재하지 않는 회원 정보로 토큰 발급을 요청할 경우
             * Then  토큰이 발급되지 않는다.
             */
            @Test
            void 존재하지_않는_회원의_토큰_발급_요청() {
                // given
                String email = "admin@email.com";
                String password = "password";
                int age = 20;

                // when, then
                assertThatExceptionOfType(NotFoundMemberException.class)
                        .isThrownBy(() -> {
                            tokenService.createToken(email, password);
                        }).withMessageMatching("회원 정보가 없습니다.");
            }
        }
    }
}