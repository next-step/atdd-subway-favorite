package nextstep.utils;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class GithubTestRepository {
    private final Map<String, GithubTestUser> USER_REPOSITORY = new HashMap<>();

    public void addUser(String code, GithubTestUser user) {
        if (isUserExist(code)) {
            throw new IllegalArgumentException("이미 존재하는 code");
        }

        USER_REPOSITORY.put(code, user);
    }

    public boolean isUserExist(String code) {
        return USER_REPOSITORY.containsKey(code);
    }

    public GithubTestUser findByCode(String code) {
        return USER_REPOSITORY.get(code);
    }
}
