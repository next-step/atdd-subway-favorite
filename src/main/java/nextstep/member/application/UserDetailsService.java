package nextstep.member.application;

import nextstep.auth.application.dto.GithubProfileResponse;

public interface UserDetailsService {
    UserDetails findMemberByEmail(String email);
    UserDetails findMemberOrCreate(GithubProfileResponse githubProfileResponse);
}
