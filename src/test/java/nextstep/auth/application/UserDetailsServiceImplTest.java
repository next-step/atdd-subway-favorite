package nextstep.auth.application;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
@Transactional
public class UserDetailsServiceImplTest {
    private final String EMAIL = "test@test.com";
    private final int AGE = 20;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
    }

    @DisplayName("이미 존재하는 회원이라면 정보 조회")
    @Test
    void findOrCreateMemberWhenMemberExist() {
        Member member = memberRepository.save(new Member(EMAIL, "", AGE));
        UserDetails userDetails = userDetailsService.findOrCreateMember(member.getEmail(), member.getAge());

        assertThat(userDetails.getEmail()).isEqualTo(member.getEmail());
    }

    @DisplayName("존재하지 않는 회원이라면 저장 후 정보 조회")
    @Test
    void findOrCreateMemberWhenMemberNotExist() {
        UserDetails userDetails = userDetailsService.findOrCreateMember(EMAIL, AGE);

        assertSoftly(softly->{
            softly.assertThat(userDetails.getId()).isNotNull();
            softly.assertThat(userDetails.getEmail()).isEqualTo(EMAIL);
            softly.assertThat(userDetails.getAge()).isEqualTo(AGE);
        });
    }
}
