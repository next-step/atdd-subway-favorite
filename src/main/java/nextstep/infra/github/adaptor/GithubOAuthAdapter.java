package nextstep.infra.github.adaptor;

import nextstep.infra.github.dto.GithubProfileResponse;

public interface GithubOAuthAdapter {
    GithubProfileResponse login(String code);
}
