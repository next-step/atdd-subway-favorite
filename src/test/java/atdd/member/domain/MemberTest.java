package atdd.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void isMatchPassword() {
        String password = "1234";
        Member m = new Member("soek2@kakao.com", password);
        assertThat(m.isMatchPassword(password)).isTrue();
    }
}