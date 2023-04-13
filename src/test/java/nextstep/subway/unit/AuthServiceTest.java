package nextstep.subway.unit;

import nextstep.exception.MemberInvalidException;
import nextstep.exception.MemberNotFoundException;
import nextstep.member.application.AuthService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("토큰 API Test")
@SpringBootTest
@Transactional
class AuthServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuthService authService;
    private Member member;

    @BeforeEach
    void setUp() {
        Member admin = new Member("admin@test.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name()));
        member = memberRepository.save(admin);
    }

    @Test
    @DisplayName("이메일과 패스워드를 입력하면 토큰을 반환한다.")
    void loginTest() {
        // given
        TokenRequest tokenRequest = new TokenRequest(member.getEmail(), member.getPassword());

        // when
        TokenResponse tokenResponse = authService.login(tokenRequest);

        // then
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({"member@test.com, password", "member2@test.com, password"})
    @DisplayName("DB에 등록되지 않은 멤버면 에러를 발생한다.")
    void unregisterMemberLoginTest(String newEmail, String password) {
        // given
        TokenRequest tokenRequest = new TokenRequest(newEmail, password);

        // when
        // then
        assertThatThrownBy(() -> authService.login(tokenRequest))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining("등록된 멤버가 아닙니다.");
    }

    @ParameterizedTest
    @CsvSource({"admin@test.com, 1234password", "admin@test.com, @#$123",})
    @DisplayName("비밀번호가 일치하지 않을 경우 에러를 발생한다.")
    void mismatchPasswordLoginTest(String email, String wrongPassword) {
        // given
        TokenRequest tokenRequest = new TokenRequest(email, wrongPassword);

        // when
        // then
        assertThatThrownBy(() -> authService.login(tokenRequest))
                .isInstanceOf(MemberInvalidException.class)
                .hasMessageContaining("유효한 멤버가 아닙니다.");
    }
}
