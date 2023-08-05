package nextstep.auth.application;

import nextstep.auth.token.TokenResponse;
import nextstep.auth.token.TokenService;
import nextstep.exception.AuthenticationException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class TokenServiceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
    }

    @DisplayName("등록된 회원에 이메일과 패스워드가 정상이라면 토큰을 리턴한다.")
    @Test
    void createToken() {
        // when
        TokenResponse token = tokenService.createToken(EMAIL, PASSWORD);

        // then
        assertThat(token.getAccessToken()).isNotBlank();
    }

    @DisplayName("등록된 회원에 패스워드가 일치하지 않을 경우 에러를 던진다.")
    @Test
    void createToken_not_match_password() {
        // when then
        assertThatThrownBy(() -> tokenService.createToken(EMAIL, ""))
                .isExactlyInstanceOf(AuthenticationException.class);
    }

    @DisplayName("등록된 회원이 조회되지 않을경우 에러를 던진다.")
    @Test
    void createToken_not_found_user() {
        // when then
        assertThatThrownBy(() -> tokenService.createToken("", ""))
                .isExactlyInstanceOf(AuthenticationException.class);
    }

}
