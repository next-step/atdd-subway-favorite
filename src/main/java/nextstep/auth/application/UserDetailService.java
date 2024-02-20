package nextstep.auth.application;

import nextstep.auth.application.dto.GithubProfileResponse;

public interface UserDetailService {
    UserDetails findByEmail(String email);

    UserDetails findMemberOrCreate(GithubProfileResponse githubProfileResponse);
}
