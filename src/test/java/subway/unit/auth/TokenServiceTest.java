package subway.unit.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.auth.token.TokenResponse;
import subway.auth.token.TokenService;
import subway.exception.AuthenticationException;
import subway.member.domain.Member;
import subway.member.domain.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("TokenService 단위 테스트 (spring integration test)")
@SpringBootTest
@Transactional
public class TokenServiceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private MemberRepository memberRepository;


    /**
     * Given 로그인 정보가 있고
     * When 로그인 정보가 올바르면
     * Then 토큰이 발급된다.
     */
    @DisplayName("토큰 발급")
    @Test
    void createToken() {
        // given
        Member member = Member.builder().email(EMAIL).password(PASSWORD).age(AGE).build();
        memberRepository.save(member);

        // when
        TokenResponse token = tokenService.createToken(EMAIL, PASSWORD);

        // then
        assertThat(token.getAccessToken()).isNotBlank();
    }

    /**
     * Given 로그인 정보가 있고
     * When 비밀번호가 일치하지 않는 정보로 요청하면
     * Then 토큰이 생성되지 않는다.
     */
    @DisplayName("잘못된 정보를 포함한 토큰 발급")
    @Test
    void createTokenWithNotMatchPassword() {
        // given
        Member member = Member.builder().email(EMAIL).password(PASSWORD).age(AGE).build();
        memberRepository.save(member);

        // when
        assertThatThrownBy(() -> tokenService.createToken(EMAIL, "FAILED_PASSWORD"))
                .isInstanceOf(AuthenticationException.class);

    }
}
