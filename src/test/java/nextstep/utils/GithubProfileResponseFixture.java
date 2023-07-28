package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubProfileResponse;

public class GithubProfileResponseFixture {

    private static final Integer age = 27;

    public static GithubProfileResponse of(String email) {
        return new GithubProfileResponse(email, age);
    }

}
