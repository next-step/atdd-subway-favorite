package nextstep.core.member.application;

import nextstep.common.annotation.ApplicationTest;
import nextstep.core.member.application.dto.MemberResponse;
import nextstep.core.auth.domain.LoginMember;
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

        final String EXISTENT_EMAIL = "admin001@email.com";
        final String NON_EXISTENT_EMAIL = "admin100@email.com";

        Member savedMember;

        @BeforeEach
        void 사전_회원_생성() {
            String password = "password!";
            int age = 30;

            savedMember = memberRepository.save(new Member(EXISTENT_EMAIL, password, age));
            memberRepository.save(new Member("admin002@email.com", password, age));
            memberRepository.save(new Member("admin003@email.com", password, age));
            memberRepository.save(new Member("admin004@email.com", password, age));
            memberRepository.save(new Member("admin005@email.com", password, age));
            memberRepository.save(new Member("admin006@email.com", password, age));
        }

        @Nested
        class 성공 {
            /**
             * Given 회원을 생성한다.
             * When  생성된 회원의 이메일을 통해 내 정보를 조회한다.
             * Then  내 정보를 조회할 수 있다
             */
            @Test
            void 내_이메일로_내_정보_조회() {
                // when
                MemberResponse me = memberService.findMe(new LoginMember(EXISTENT_EMAIL));

                // then
                assertThat(me.getEmail()).isEqualTo(EXISTENT_EMAIL);
                assertThat(me.getAge()).isEqualTo(30);
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
                // when, then
                assertThatExceptionOfType(NotFoundMemberException.class)
                        .isThrownBy(() -> {
                            memberService.findMe(new LoginMember(NON_EXISTENT_EMAIL));
                        })
                        .withMessageMatching("회원 정보가 없습니다.");
            }
        }
    }
    @Nested
    class 회원_조회_혹은_생성 {

        final String EXISTENT_EMAIL = "admin001@email.com";

        Member savedMember;

        @BeforeEach
        void 사전_회원_생성() {
            String password = "password!";
            int age = 30;

            savedMember = memberRepository.save(new Member(EXISTENT_EMAIL, password, age));
            memberRepository.save(new Member("admin002@email.com", password, age));
            memberRepository.save(new Member("admin003@email.com", password, age));
            memberRepository.save(new Member("admin004@email.com", password, age));
            memberRepository.save(new Member("admin005@email.com", password, age));
            memberRepository.save(new Member("admin006@email.com", password, age));
        }

        /**
         * Given 회원을 추가한다.
         * When  추가된 회원의 이메일을 통해 회원 정보를 조회할 경우
         * Then  회원 정보를 조회할 수 있다.
         */
        @Test
        void 기존_생성된_회원_조회() {
            // when
            Member member = memberService.findOrCreate(EXISTENT_EMAIL);

            // then
            assertThat(savedMember).usingRecursiveComparison()
                    .isEqualTo(member);
        }

        /**
         * When  이메일을 통해 회원 정보를 조회할 때
         * When     이메일이 동일한 회원이 없을 경우
         * Then  회원이 추가되고, 추가된 회원 정보를 조회할 수 있다.
         */
        @Test
        void 새로운_회원_추가() {
            // when
            Member member = memberService.findOrCreate("admin100@email.com");

            // then
            assertThat(member.getEmail()).isEqualTo("admin100@email.com");
            assertThat(member.getPassword()).isNotBlank();
            assertThat(member.getAge()).isNull();
        }
    }
}
