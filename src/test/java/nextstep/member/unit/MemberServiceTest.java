package nextstep.member.unit;

import nextstep.authentication.application.dto.GithubProfileResponse;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.DatabaseCleanup;
import org.assertj.core.api.ThrowableAssert.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.utils.GithubResponses.사용자1;
import static nextstep.utils.GithubResponses.사용자2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("회원 서비스 관련 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class MemberServiceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    Member 회원1;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute(this);
        회원1 = new Member(1L, 사용자1.getEmail(), 사용자1.getPassword(), 사용자1.getAge());
        memberRepository.save(회원1);
    }

    @DisplayName("이메일로 회원을 조회하는 함수는, 주어진 이메일로 회원을 조회한다.")
    @Test
    void findByEmailTest() {
        // when
        Member actual = memberService.findMemberByEmail(회원1.getEmail());

        // then
        assertThat(actual).isEqualTo(회원1);
    }

    @DisplayName("이메일로 회원을 조회하는 함수는, 주어진 이메일로 회원을 조회한다.")
    @Test
    void memberNotFoundExceptionTest() {
        // when
        ThrowingCallable actual = () -> memberService.findMemberByEmail(사용자2.getEmail());

        // then
        assertThatThrownBy(actual).isInstanceOf(RuntimeException.class);
    }


    @DisplayName("회원을 찾거나 생성하는 함수는, 주어진 깃헙 프로필 정보로 회원을 조회한다.")
    @Test
    void lookUpOrCreateTest() {
        // when
        Member actual = memberService.lookUpOrCreateMember(new GithubProfileResponse(사용자1.getEmail(), 사용자1.getAge()));

        // then
        assertThat(actual).isEqualTo(회원1);
    }

    @DisplayName("회원을 찾거나 생성하는 함수는, 주어진 깃헙 프로필 정보로 조회된 회원이 없으면 회원을 생성한다.")
    @Test
    void lookUpOrCreateSaveTest() {
        // when
        Member member = memberService.lookUpOrCreateMember(new GithubProfileResponse(사용자2.getEmail(), 사용자2.getAge()));

        // then
        Member expected = memberService.findMemberByEmail(member.getEmail());
        assertThat(member).isEqualTo(expected);
    }
}
