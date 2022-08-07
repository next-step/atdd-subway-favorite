package nextstep.subway.unit.auth;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import nextstep.auth.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.auth.service.CustomUserDetails;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.MemberRepository;

@ExtendWith(MockitoExtension.class)
public class UsernamePasswordAuthenticationFilterTest {
	private static final String PRINCIPAL_NAME = "username";
	private static final String CREDENTIAL_NAME = "password";
	private static final String EMAIL = "email@email.com";
	private static final String PASSWORD = "password";
	private static final int AGE = 20;
	private static final List<String> ROLES = Arrays.asList("ROLE_ADMIN");

	@Mock
	private MemberRepository memberRepository;
	@Mock
	private CustomUserDetails customUserDetails;
	@Autowired
	private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;

	@BeforeEach
	void setUp() {
		customUserDetails = new CustomUserDetailsService(memberRepository);
		usernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter(customUserDetails);

	}

	/*
		@Test
		void convert() {
			//when
			AuthenticationToken authenticationToken = usernamePasswordAuthenticationFilter.convert(createMockRequest());

			//then
			assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
			assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
		}

		@Test
		void authenticate() {
			//given
			AuthenticationToken authenticationToken = usernamePasswordAuthenticationFilter.convert(createMockRequest());
			when(memberRepository.findByEmail(EMAIL))
				.thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE, ROLES)));

			//when
			Authentication authentication = usernamePasswordAuthenticationFilter.authenticate(authenticationToken);

			//then
			assertThat(authentication.getPrincipal()).isEqualTo(EMAIL);
			assertThat(authentication.getAuthorities()).isEqualTo(ROLES);

		}
	*/
	private MockHttpServletRequest createMockRequest() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter(PRINCIPAL_NAME, EMAIL);
		request.addParameter(CREDENTIAL_NAME, PASSWORD);

		return request;
	}
}
