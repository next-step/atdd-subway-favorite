package atdd.path.application;

import atdd.path.application.dto.UserResponseView;
import atdd.path.dao.MemberDao;
import atdd.path.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = UserService.class)
class UserServiceTest {

	private UserService userService;

	@MockBean
	private MemberDao memberDao;

	@MockBean
	private JwtTokenProvider jwtTokenProvider;

	@BeforeEach
	void setUp() {
		userService = new UserService(memberDao, jwtTokenProvider);
	}

	@Test
	void retrieveMyInfo() {
		// given
		given(jwtTokenProvider.resolveToken(any(HttpServletRequest.class)))
			.willReturn(TOKEN_1);
		given(jwtTokenProvider.validateToken(TOKEN_1))
			.willReturn(true);
		given(jwtTokenProvider.getUserEmail(TOKEN_1))
			.willReturn(MEMBER_EMAIL);
		given(memberDao.findByEmail(MEMBER_EMAIL))
			.willReturn(Optional.of(new Member(MEMBER_ID, MEMBER_EMAIL, MEMBER_NAME, MEMBER_PASSWORD)));

		// when
		UserResponseView response = userService.retrieveMyInfo(new MockHttpServletRequest());

		// then
		assertThat(response.getEmail()).isEqualTo(MEMBER_EMAIL);
		assertThat(response.getPassword()).isEqualTo(MEMBER_PASSWORD);
	}
}