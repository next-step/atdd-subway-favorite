package nextstep.auth.model.authentication.service;

import nextstep.auth.model.authentication.UserDetails;
import nextstep.subway.domain.member.Member;
import nextstep.subway.domain.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserDetailsServiceTest {
    private final String EMAIL = "login@email.com";
    private final String PASSWORD = "password";
    private final int AGE = 26;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원을 디비로부터 조회한다.")
    void loadUserByUsername() {
        // given
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        // when
        UserDetails userDetails = userDetailsService.loadUserByUsername(EMAIL);

        // then
        assertThat(userDetails.getUsername()).isEqualTo(EMAIL);
    }
}