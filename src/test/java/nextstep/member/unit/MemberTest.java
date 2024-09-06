package nextstep.member.unit;

import nextstep.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.utils.UserInformation.사용자1;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("회원 관련 테스트")
public class MemberTest {

    @DisplayName("비밀번호 확인 함수는, 입력받은 비밀번호와 회원의 비밀번호가 같으면 true를 반환한다.")
    @Test
    void checkPasswordTest() {
        // given
        Member member = new Member(사용자1.getEmail(), 사용자1.getPassword(), 사용자1.getAge());

        // when & then
        assertThat(member.checkPassword(사용자1.getPassword())).isTrue();
    }
}
