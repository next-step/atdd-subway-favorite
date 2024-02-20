package nextstep.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("회원 도메인 테스트")
class MemberTest {

    private final String email = "test@email.com";
    private final String password = "password";
    private final int age = 20;

    @DisplayName("회원을 생성한다.")
    @Test
    void createMember() {
        Member member = new Member(email, password, age);

        assertAll(
                () -> assertEquals(email, member.getEmail()),
                () -> assertEquals(password, member.getPassword()),
                () -> assertEquals(age, member.getAge()),
                () -> assertTrue(member.checkPassword(password))
        );
    }

    @DisplayName("회원을 수정한다.")
    @Test
    void updateMember() {
        Member member = new Member(email, password, age);
        String updateEmail = "update@email.com";
        String updatePassword = "update_password";
        int updateAge = 30;
        Member updateMember = new Member(updateEmail, updatePassword, updateAge);

        member.update(updateMember);

        assertAll(
                () -> assertEquals(updateEmail, member.getEmail()),
                () -> assertEquals(updatePassword, member.getPassword()),
                () -> assertEquals(updateAge, member.getAge()),
                () -> assertTrue(member.checkPassword(updatePassword))
        );
    }
}
