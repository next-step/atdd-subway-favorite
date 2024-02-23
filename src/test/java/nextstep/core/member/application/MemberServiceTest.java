package nextstep.core.member.application;

import nextstep.common.annotation.ApplicationTest;
import nextstep.core.member.application.dto.MemberResponse;
import nextstep.core.member.domain.LoginMember;
import nextstep.core.member.domain.Member;
import nextstep.core.member.domain.MemberRepository;
import nextstep.core.member.exception.NotFoundMemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@DisplayName("회원 서비스 레이어 테스트")
@ApplicationTest
public class MemberServiceTest {

    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void 사전_회원_서비스_객체_생성() {
        memberService = new MemberService(memberRepository);
    }

    @Nested
    class 내_정보_조회 {

        @Nested
        class 성공 {
            /**
             * Given 회원을 생성한다.
             * When  생성된 회원의 이메일을 통해 내 정보를 조회한다.
             * Then  내 정보를 조회할 수 있다
             */
            @Test
            void 내_이메일로_내_정보_조회() {
                // given
                String email = "admin@email.com";
                String password = "password";
                int age = 20;

                memberRepository.save(new Member(email, password, age));

                // when
                MemberResponse me = memberService.findMe(new LoginMember(email));

                // then
                assertThat(me.getEmail()).isEqualTo(email);
                assertThat(me.getAge()).isEqualTo(age);
            }
        }

        @Nested
        class 실패 {
            /**
             * Given 회원을 생성한다.
             * When  생성된 회원의 이메일이 아닌, 존재하지 않은 이메일을 가진 회원을 조회할 경우
             * Then  내 정보를 조회할 수 없다.
             */
            @Test
            void 존재하지_않는_이메일을_가진_회원_조회() {
                // given
                String nonExistentEmail  = "admin@email.com";

                // when, then
                assertThatExceptionOfType(NotFoundMemberException.class)
                        .isThrownBy(() -> {
                            memberService.findMe(new LoginMember(nonExistentEmail));
                        })
                        .withMessageMatching("회원 정보가 없습니다.");
            }
        }
    }
}
