package nextstep.member.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRegisterTest {

    @Autowired
    private MemberRegister memberRegister;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 회원이_존재할경우_회원정보를_조회한다() {
        Member email = memberRepository.save(new Member("email"));
        Member findMember = memberRegister.findOrCreate(email.getEmail());

        List<Member> members = memberRepository.findAll();
        assertAll(
                () -> assertThat(members).hasSize(1),
                () -> assertThat(findMember.getEmail()).isEqualTo(email.getEmail())
        );
    }

    @Test
    void 회원이_존재하지_않을경우_회원을_생성한_후에_회원정보를_조회한다() {
        Member findMember = memberRegister.findOrCreate("email");

        List<Member> members = memberRepository.findAll();
        assertAll(
                () -> assertThat(members).hasSize(1),
                () -> assertThat(findMember.getEmail()).isEqualTo("email")
        );
    }

}
