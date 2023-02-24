package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.NativeWebRequest;

import nextstep.auth.AuthMember;
import nextstep.auth.AuthMemberArgumentResolver;
import nextstep.auth.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
public class ArgumentResolverMockTest {

	private final String TEST_TOKEN = "ATDD_SUBWAY_FAVORITE_codeHousePig";
	private final String AUTH_HEADER = "Bearer " + TEST_TOKEN;
	private final String NOT_AUTH_HEADER = "noAuth " + TEST_TOKEN;

	private String EMAIL = "test@test.com";
	private List<String> ROLES = Arrays.asList("admin");

	@Mock
	private NativeWebRequest request;
	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@InjectMocks
	private AuthMemberArgumentResolver argumentResolver;

	@DisplayName("토큰 값으로 회원 정보를 조회할 수 있다")
	@Test
	void resolveArgumentTest() throws Exception {
		// given
		when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(AUTH_HEADER);
		when(jwtTokenProvider.validateToken(TEST_TOKEN)).thenReturn(true);
		when(jwtTokenProvider.getPrincipal(TEST_TOKEN)).thenReturn(EMAIL);
		when(jwtTokenProvider.getRoles(TEST_TOKEN)).thenReturn(ROLES);

		// when
		AuthMember authMember = (AuthMember)argumentResolver.resolveArgument(null, null, request, null);

		// then
		assertThat(authMember.getEmail()).isEqualTo(EMAIL);
		assertThat(authMember.getRoles().get(0)).contains(ROLES);
	}

	@DisplayName("올바르지 않은 토큰 값으로 회원 정보를 조회할 수 없다")
	@Test
	void notResolveArgumentTest() {
		// when
		when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(NOT_AUTH_HEADER);

		// then
		assertThrows(IllegalArgumentException.class,
			() -> argumentResolver.resolveArgument(null, null, request, null)
		);
	}
}
