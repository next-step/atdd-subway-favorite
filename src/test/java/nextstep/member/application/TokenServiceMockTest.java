package nextstep.member.application;

import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.ProfileResponse;
import nextstep.member.domain.Member;
import nextstep.utils.FakeClientRequester;
import nextstep.utils.FakeMemberServiceImpl;
import nextstep.utils.FakeTokenProvider;
import nextstep.utils.GithubResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TokenServiceMockTest {

    @Mock
    private TokenService tokenService;
    private ProfileResponse 사용자1;
    private Member member_사용자1;
    private String 비밀번호 = "password";
    private String 틀린_비밀번호 = "!password";
    private String FAKE_토큰 = "createToken_success";
    private String 코드 ="code";

    @BeforeEach
    void setup() {
        tokenService = new TokenService(new FakeMemberServiceImpl(), new FakeTokenProvider(), new FakeClientRequester());

        사용자1 = ProfileResponse.of(GithubResponse.사용자1.getEmail(), GithubResponse.사용자1.getAge());
        member_사용자1 = Member.of(1L, GithubResponse.사용자1.getEmail(), "password", GithubResponse.사용자1.getAge());

    }
    @DisplayName("[createToken] 이메일과 비밀번호를 통해 Token을 생성한다.")
    @Test
    public void createToken_success() {
        // when
        var 생성된_토큰 = tokenService.createToken(사용자1.getEmail(), 비밀번호);

        // then
        assertThat(생성된_토큰.getAccessToken()).isEqualTo(FAKE_토큰);
    }

    @DisplayName("[createToken] 이메일과 틀린 비밀번호를 통해 Token 생성을 시도하면 예외가 발생한다.")
    @Test
    public void createToken_fail() {
        // when & then
        assertThrows(AuthenticationException.class, () -> tokenService.createToken(사용자1.getEmail(), 틀린_비밀번호));
    }

    @DisplayName("[getAuthToken] code를 통해 토큰을 발급받는다.")
    @Test
    public void getAuthToken_success() {
        // when
        var 생성된_토큰 = tokenService.getAuthToken(코드);

        // then
        assertThat(생성된_토큰.getAccessToken()).isEqualTo(FAKE_토큰);

    }

    @DisplayName("[findOrCreateMember] 사용자가 조회되지 않으면, 사용자를 저장한 다음 저장한 값을 반환한다.")
    @Test
    public void memberDoesNotExist() {
        // when
        var 멤버_응답 = tokenService.findOrCreateMember(ProfileResponse.of(사용자1.getEmail(), 사용자1.getAge()));

        // then
        assertAll(
                () -> assertThat(멤버_응답.getEmail()).isEqualTo(사용자1.getEmail()),
                () -> assertThat(멤버_응답.getAge()).isEqualTo(사용자1.getAge()),
                () -> assertThat(멤버_응답.getId()).isNotNull()
        );
    }

    @DisplayName("[findOrCreateMember] 사용자를 조회한 다음, 조회된 사용자를 반환한다.")
    @Test
    public void memberExist() {
        // when
        var 멤버_응답 = tokenService.findOrCreateMember(사용자1);

        // then
        assertAll(
                () -> assertThat(멤버_응답.getEmail()).isEqualTo(사용자1.getEmail()),
                () -> assertThat(멤버_응답.getAge()).isEqualTo(사용자1.getAge()),
                () -> assertThat(멤버_응답.getId()).isNotNull()
        );
    }

}

