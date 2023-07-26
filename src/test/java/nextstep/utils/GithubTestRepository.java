package nextstep.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import org.springframework.stereotype.Repository;

@Repository
public class GithubTestRepository {
    private final Map<String, GithubTestUser> USER_REPOSITORY = new HashMap<>();

    public GithubTestRepository() {
        IntStream.rangeClosed(1, 3)
                .forEach(num -> USER_REPOSITORY.put("code" + num, new GithubTestUser("email" + num, num)));
    }
}
