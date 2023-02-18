package nextstep.subway.acceptance;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@ActiveProfiles("test")
@TestPropertySource("classpath:/application-test.properties")
@SpringBootTest(webEnvironment = DEFINED_PORT)
public abstract class ApplicationContextTest {
}
