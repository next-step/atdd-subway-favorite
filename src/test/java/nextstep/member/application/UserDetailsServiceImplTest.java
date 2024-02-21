package nextstep.member.application;

import nextstep.auth.application.UserDetails;
import nextstep.auth.application.UserDetailsService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDetailsServiceImplTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 회원이_존재할경우_회원정보를_조회한다() {
        Member email = memberRepository.save(new Member("email"));
        UserDetails userDetails = userDetailsService.findOrCreate(email.getEmail());

        List<Member> members = memberRepository.findAll();
        assertAll(
                () -> assertThat(members).hasSize(1),
                () -> AssertionsForClassTypes.assertThat(userDetails.getEmail()).isEqualTo(email.getEmail())
        );
    }

    @Test
    void 회원이_존재하지_않을경우_회원을_생성한_후에_회원정보를_조회한다() {
        UserDetails userDetails = userDetailsService.findOrCreate("email");

        List<Member> members = memberRepository.findAll();
        assertAll(
                () -> assertThat(members).hasSize(1),
                () -> AssertionsForClassTypes.assertThat(userDetails.getEmail()).isEqualTo("email")
        );
    }

}
