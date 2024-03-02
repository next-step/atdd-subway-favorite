package nextstep.auth.application;

import nextstep.auth.application.dto.GithubMemberRequest;
import nextstep.member.domain.Member;

import java.util.Optional;

public interface UserDetailService {
    Member findMemberByEmailOrElseThrow(String email);

    Optional<Member> findMemberByEmail(String email);

    void createGithubMember(GithubMemberRequest githubMemberRequest);
}
