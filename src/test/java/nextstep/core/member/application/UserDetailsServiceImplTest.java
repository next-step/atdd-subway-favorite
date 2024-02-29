package nextstep.core.member.application;

import nextstep.common.annotation.ApplicationTest;
import nextstep.core.member.application.dto.MemberRequest;
import nextstep.core.member.exception.NotFoundMemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.core.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("유저 서비스 레이어 테스트")
@ApplicationTest
public class UserDetailsServiceImplTest {

    UserDetailsServiceImpl userDetailsService;

    @Autowired
    MemberService memberService;


    @BeforeEach
    void 사전_유저_서비스_생성() {
        userDetailsService = new UserDetailsServiceImpl(memberService);
    }

    @Nested
    class 토큰_발급 {
        @Nested
        class 성공 {
            /**
             * Given 회원을 생성한다.
             * When  해당 회원의 이메일과 비밀번호를 확인할 경우
             * Then  정상적으로 확인된다.
             */
            @Test
            void 저장된_회원으로_확인_요청() {
                // given
                MemberRequest memberRequest = new MemberRequest(브라운.이메일, 브라운.비밀번호, 브라운.나이);
                memberService.createMember(memberRequest);

                // when
                boolean actual = userDetailsService.verifyUser(브라운.이메일, 브라운.비밀번호);

                // then
                assertThat(actual).isTrue();
            }
        }

        @Nested
        class 실패 {
            /**
             * Given 회원을 생성한다.
             * When  해당 회원의 비밀번호가 아닌 다른 비밀번호로 확인할 경우
             * Then  확인할 수 없다.
             */
            @Test
            void 비밀번호가_다른_회원의_확인_요청() {
                // given
                String changedPassword = "changed password";

                MemberRequest memberRequest = new MemberRequest(스미스.이메일, 스미스.비밀번호, 스미스.나이);
                memberService.createMember(memberRequest);

                // when, then
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                            userDetailsService.verifyUser(스미스.이메일, changedPassword);
                        }).withMessageMatching("비밀번호가 다릅니다.");
            }

            /**
             * Given 저장소에 저장하지 않는 회원을 생성한다.
             * When  회원 저장소에 존재하지 않는 회원 정보를 확인할 경우
             * Then  확인할 수 없다.
             */
            @Test
            void 존재하지_않는_회원의_확인_요청() {
                // when, then
                assertThatExceptionOfType(NotFoundMemberException.class)
                        .isThrownBy(() -> {
                            userDetailsService.verifyUser(존슨.이메일, 존슨.비밀번호);
                        }).withMessageMatching("회원 정보가 없습니다.");
            }

        }
    }
};
