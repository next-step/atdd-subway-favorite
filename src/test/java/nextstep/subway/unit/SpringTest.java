package nextstep.subway.unit;

import nextstep.subway.GithubClientTestContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(GithubClientTestContextConfiguration.class)
public abstract class SpringTest {
}
