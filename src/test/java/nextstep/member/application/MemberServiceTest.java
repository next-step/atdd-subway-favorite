package nextstep.member.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("회원 관련 기능")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private Member member;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();

        this.member = new Member(
                "email.google.com", "1234", 26,
                List.of(RoleType.ROLE_MEMBER.name(), RoleType.ROLE_ADMIN.name())
        );
    }

    @DisplayName("토큰을 발급한다.")
    @Test
    void createToken() {
        memberRepository.save(member);
        String expected = jwtTokenProvider.createToken(member);
        TokenRequest token = new TokenRequest(member.getEmail(), member.getPassword());

        TokenResponse tokenResponse = memberService.createTokenFrom(token);

        assertThat(tokenResponse.getAccessToken()).isEqualTo(expected);
    }

    @DisplayName("토큰으로 멤버를 조회한다.")
    @Test
    void findMember() {
        memberRepository.save(member);
        String token = jwtTokenProvider.createToken(member);

        MemberResponse member = memberService.findMember(token);

        Assertions.assertAll(
                () -> assertThat(member.getId()).isEqualTo(member.getId()),
                () -> assertThat(member.getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(member.getAge()).isEqualTo(member.getAge())
        );
    }
}
