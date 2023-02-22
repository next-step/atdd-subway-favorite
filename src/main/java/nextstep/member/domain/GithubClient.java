package nextstep.member.domain;

import java.util.Optional;

public interface GithubClient {

    Optional<String> getAccessTokenFromGithub(String code);
}
