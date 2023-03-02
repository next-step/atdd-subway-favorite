package nextstep.auth.domain;

import nextstep.auth.dto.AuthMember;

public interface AuthService {
    AuthMember findMember(String header);

    void validate(String header);

    String getPrefix();
}
