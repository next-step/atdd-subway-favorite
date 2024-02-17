package nextstep.member.unit;

import nextstep.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class MemberTest {

    @Test
    void updateMember() {
        final Member member = new Member("qwe5507@gmail.com", "1234", 31);
        final Member modifyMember = new Member("apvmffkdls@gmail.com", "5678", 26);
        member.update(modifyMember);

        assertThat(member).isEqualTo(modifyMember);
    }

    @DisplayName("비밀번호가 같으면 True를 리턴한다.")
    @Test
    void checkPassword() {
        final Member member = new Member("qwe5507@gmail.com", "1234", 31);
        assertThat(member.isSamePassword("1234")).isTrue();
    }

    @DisplayName("비밀번호가 다르면 False를 리턴한다.")
    @Test
    void checkPassword_invalid() {
        final Member member = new Member("qwe5507@gmail.com", "1234", 31);
        assertThat(member.isSamePassword("5678")).isFalse();
    }
}
