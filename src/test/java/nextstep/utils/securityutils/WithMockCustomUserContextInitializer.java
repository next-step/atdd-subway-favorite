package nextstep.utils.securityutils;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;

/**
 * @author : Rene Choi
 * @since : 2024/02/13
 */
public class WithMockCustomUserContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
	@Override
	public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
		TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
			configurableApplicationContext,
			"test.execution.listener=nextstep.utils.securityutils.WithMockCustomUserTestExecutionListener"
		);
	}
}