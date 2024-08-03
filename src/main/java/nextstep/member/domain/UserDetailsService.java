package nextstep.member.domain;

import nextstep.member.UsernameNotFoundException;
import nextstep.member.application.dto.GithubProfileResponse;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    UserDetails loadUserByGithubProfile(GithubProfileResponse githubProfile) throws UsernameNotFoundException;
}
