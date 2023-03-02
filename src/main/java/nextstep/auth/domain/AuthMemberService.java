package nextstep.auth.domain;

import nextstep.auth.dto.AuthMember;

public interface AuthMemberService {

    AuthMember findJwtMember(String token);

    AuthMember findGitHubMember(String token);
}
