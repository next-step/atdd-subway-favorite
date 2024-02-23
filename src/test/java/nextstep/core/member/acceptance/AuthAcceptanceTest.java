package nextstep.core.member.acceptance;

import nextstep.common.annotation.AcceptanceTest;
import nextstep.core.member.domain.Member;
import nextstep.core.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.core.member.step.AuthSteps.성공하는_토큰_발급_요청;
import static nextstep.core.member.step.AuthSteps.실패하는_토큰_발급_요청;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("인증 인수 테스트")
@AcceptanceTest
class AuthAcceptanceTest {

    @Autowired
    private MemberRepository 회원_저장소;

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
                var 회원 = 회원_생성("admin@email.com", "password", 20);
                회원_저장소_저장(회원);

                // when
                var 발급된_토큰 = 성공하는_토큰_발급_요청(회원);

                // then
                토큰_확인(발급된_토큰);
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
                var 회원 = 회원_생성("admin@email.com", "password", 20);
                회원_저장소_저장(회원);

                // when
                회원_비밀번호_수정(회원, "change password");

                // then
                실패하는_토큰_발급_요청(회원);
            }

            /**
             * Given 저장소에 저장하지 않는 회원을 생성한다.
             * When  회원 저장소에 존재하지 않는 회원 정보로 토큰 발급을 요청할 경우
             * Then  토큰이 발급되지 않는다.
             */
            @Test
            void 존재하지_않는_회원의_토큰_발급_요청() {
                // given
                var 저장소에_저장하지_않은_회원 = 회원_생성("admin@email.com", "password", 20);

                // when, then
                실패하는_토큰_발급_요청(저장소에_저장하지_않은_회원);
            }
        }
    }

    private static void 회원_비밀번호_수정(Member 회원, String 변경할_비밀번호) {
        회원.update(회원_생성(회원.getEmail(), 변경할_비밀번호, 회원.getAge()));
    }

    public static void 토큰_확인(String 발급된_토큰) {
        assertThat(발급된_토큰).isNotBlank();
    }

    public void 회원_저장소_저장(Member 회원) {
        회원_저장소.save(회원);
    }

    public static Member 회원_생성(String 이메일, String 비밀번호, int 나이) {
        return new Member(이메일, 비밀번호, 나이);
    }
}