package nextstep.member.application;

import nextstep.member.domain.PasswordCheckableUser;

public interface PasswordCheckableUserService {
    PasswordCheckableUser loadUserByUsername(String email);
}
