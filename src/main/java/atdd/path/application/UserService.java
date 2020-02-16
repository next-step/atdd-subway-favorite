package atdd.path.application;

import atdd.path.domain.User;

public interface UserService {

    User create(User user);

    void delete(User user);
}
