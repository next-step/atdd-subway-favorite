package nextstep.member.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(new Member("aaaa", "password", 20, List.of(RoleType.ROLE_ADMIN.name())));

    }

    @Test
    void findByEmailAndPassword() {
        Optional<Member> member = memberRepository.findByEmailAndPassword("aaaa", "password");
        assertThat(member.isPresent()).isTrue();
    }
}