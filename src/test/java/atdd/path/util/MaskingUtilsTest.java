package atdd.path.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MaskingUtilsTest {

	@Test
	void getMasked() {
		// given
		String password = "1234abcd!";

		// when
		String masked = MaskingUtils.getMasked(password);

		// then
		assertThat(masked.length()).isEqualTo(password.length());
		assertThat(masked).isEqualTo("*".repeat(password.length()));
	}
}