package nextstep.auth.model.authentication.service;

import nextstep.subway.domain.member.Member;
import nextstep.subway.domain.member.MemberAdaptor;
import nextstep.subway.domain.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CustomUserDetailsServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void init() {
        Member member = new Member("login@email.com", "password", 26);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("DB에 주어진 principle 이 존재하는지 확인한다.")
    void loadUserByUsername() {
        // when
        MemberAdaptor memberAdaptor = userDetailsService.loadUserByUsername("login@email.com");

        // then
        assertThat(memberAdaptor.getEmail()).isEqualTo("login@email.com");
        assertThat(memberAdaptor.getAge()).isEqualTo(26);
    }
}