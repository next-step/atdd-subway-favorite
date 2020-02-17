package atdd.path.repository;

import atdd.path.domain.User;

public class UserRepository {
    public User save(User newUser) {
        return new User("mock@email.net", "mock");
    }
}
