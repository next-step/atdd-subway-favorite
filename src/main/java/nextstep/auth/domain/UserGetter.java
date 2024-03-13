package nextstep.auth.domain;

import nextstep.auth.domain.exception.UserException;

public interface UserGetter {

    User getUser(String email) throws UserException.NotFoundUserException;

    User getUser(String email, String password);

}
