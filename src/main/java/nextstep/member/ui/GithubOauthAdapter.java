package nextstep.member.ui;

import nextstep.member.application.dto.GithubResponse;

public interface GithubOauthAdapter {

    GithubResponse login(String code);
}
