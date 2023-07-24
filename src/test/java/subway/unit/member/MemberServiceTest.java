package subway.unit.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.SubwayNotFoundException;
import subway.member.application.MemberService;
import subway.member.application.dto.MemberRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("MemberService 단위 테스트 (spring integration test)")
@SpringBootTest
@Transactional
public class MemberServiceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    @Autowired
    private MemberService memberService;

    /**
     * Given 회원 가입을 원하는 정보가 있고
     * When 회원 가입을 하면
     * Then 회원이 생성된다
     */
    @DisplayName("회원 생성")
    @Test
    void createMember() {
        // given
        var 회원_요청 = MemberRequest.builder().age(AGE).email(EMAIL).password(PASSWORD).build();

        // when
        var member = memberService.createMember(회원_요청);

        // then
        assertThat(member.getEmail()).isEqualTo(EMAIL);
        assertThat(member.getAge()).isEqualTo(AGE);
    }

    /**
     * Given 회원이 있고
     * When 회원을 찾으면
     * Then 회원이 나온다
     */
    @DisplayName("회원 찾기")
    @Test
    void findMember() {
        // given
        var 회원_요청 = MemberRequest.builder().age(AGE).email(EMAIL).password(PASSWORD).build();
        var member = memberService.createMember(회원_요청);

        // when
        var findMember = memberService.findMember(member.getId());

        // then
        assertThat(member.getEmail()).isEqualTo(EMAIL);
        assertThat(member.getAge()).isEqualTo(AGE);
    }

    /**
     * Given 회원이 있고
     * When 회원 정보를 변경하면
     * Then 정보가 변경된다
     */
    @DisplayName("회원 정보 변경")
    @Test
    void updateMember() {
        // given
        var 회원_요청 = MemberRequest.builder().age(AGE).email(EMAIL).password(PASSWORD).build();
        var member = memberService.createMember(회원_요청);

        // when
        var 회원_변경_요청 = MemberRequest.builder().age(10).email(EMAIL).password(PASSWORD).build();
        memberService.updateMember(member.getId(), 회원_변경_요청);

        // then
        var findMember = memberService.findMember(member.getId());
        assertThat(findMember.getAge()).isEqualTo(10);
    }

    /**
     * Given 회원이 있고
     * When 회원을 삭제하면
     * Then 회원이 삭제된다
     */
    @DisplayName("회원 삭제")
    @Test
    void deleteMember() {
        // given
        var 회원_요청 = MemberRequest.builder().age(AGE).email(EMAIL).password(PASSWORD).build();
        var member = memberService.createMember(회원_요청);

        // when
        memberService.deleteMember(member.getId());

        // then
        assertThatThrownBy(() -> memberService.findMember(member.getId()))
                .isInstanceOf(SubwayNotFoundException.class);
    }


}
