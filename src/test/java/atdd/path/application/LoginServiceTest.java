package atdd.path.application;

import atdd.path.application.dto.AccessTokenResponseView;
import atdd.path.application.dto.CreateAccessTokenRequestView;
import atdd.path.dao.MemberDao;
import atdd.path.domain.Member;
import atdd.path.exception.MemberNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = LoginService.class)
class LoginServiceTest {

	private LoginService loginService;

	@MockBean
	private MemberDao memberDao;

	@MockBean
	private JwtTokenProvider jwtTokenProvider;

	@BeforeEach
	void setUp() {
		loginService = new LoginService(memberDao, jwtTokenProvider);
	}

	@DisplayName("로그인 요청하면 토큰 발급")
	@Test
	void createToken() {
		// given
		CreateAccessTokenRequestView request = CreateAccessTokenRequestView.of(MEMBER_EMAIL, MEMBER_PASSWORD);
		given(memberDao.findByEmail(request.getEmail()))
			.willReturn(Optional.of(new Member(MEMBER_ID, MEMBER_EMAIL, MEMBER_NAME, MEMBER_PASSWORD)));
		given(jwtTokenProvider.createToken(request.getEmail()))
			.willReturn(TOKEN_1);

		// when
		AccessTokenResponseView response = loginService.createToken(request);

		// then
		assertThat(response.getAccessToken()).isNotNull();
		assertThat(response.getAccessToken()).isEqualTo(TOKEN_1);
	}

	@DisplayName("가입되지 않은 회원의 로그인 요청 시 예외 발생")
	@Test
	void createTokenOfUnregisteredMember() {
		// given
		CreateAccessTokenRequestView request = CreateAccessTokenRequestView.of(MEMBER_EMAIL, MEMBER_PASSWORD);

		// when
		assertThatThrownBy(() -> loginService.createToken(request))
			.isInstanceOf(MemberNotFoundException.class);
	}
}