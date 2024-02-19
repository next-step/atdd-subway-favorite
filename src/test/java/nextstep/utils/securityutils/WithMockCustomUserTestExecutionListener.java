package nextstep.utils.securityutils;

import static nextstep.fixture.MemberFixtureCreator.*;
import static nextstep.fixture.TokenFixtureCreator.*;
import static nextstep.utils.resthelper.ExtractableResponseParser.*;
import static nextstep.utils.resthelper.MemberRequestExecutor.*;
import static nextstep.utils.resthelper.TokenRequestExecutor.*;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import nextstep.api.favorite.acceptance.FavoriteAcceptanceTest;

/**
 * @author : Rene Choi
 * @since : 2024/02/13
 */
public class WithMockCustomUserTestExecutionListener extends DependencyInjectionTestExecutionListener implements TestExecutionListener {

	@Override
	public void beforeTestExecution(TestContext testContext) throws Exception {
		WithMockCustomUser withMockCustomUser = testContext.getTestMethod().getAnnotation(WithMockCustomUser.class);
		if (withMockCustomUser != null) {
			setToken(testContext, fetchTokenWithLoginExecution(withMockCustomUser));
		}
		super.beforeTestExecution(testContext);
	}

	private static String fetchTokenWithLoginExecution(WithMockCustomUser withMockCustomUser) {
		String email = withMockCustomUser.email();
		String password = withMockCustomUser.password();
		createMember(createMemberRequest(email, password, 20));
		return parseAsAccessTokenWithBearer(loginAndCreateAuthorizationToken(createTokenRequest(email, password)));
	}

	private static void setToken(TestContext testContext, String authorizationToken) {
		if (testContext.getTestClass().isAssignableFrom(FavoriteAcceptanceTest.class)) {
			((FavoriteAcceptanceTest)testContext.getTestInstance()).setAuthorizationToken(authorizationToken);
		}
	}
}