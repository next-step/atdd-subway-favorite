package nextstep.auth.application;

import nextstep.auth.domain.UserDetail;

public interface UserDetailsService {
    UserDetail getUser(String email);

    UserDetail getGithubUser(String email, Integer age);
}
