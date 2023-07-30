package nextstep.api.auth.oauth2.github;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import lombok.Getter;
import nextstep.api.auth.application.token.oauth2.github.dto.GithubAccessTokenRequest;
import nextstep.api.auth.application.token.oauth2.github.dto.GithubAccessTokenResponse;
import nextstep.api.auth.application.token.oauth2.github.dto.GithubProfileResponse;
import nextstep.api.member.domain.Member;

@Getter
public enum VirtualUsers {
    사용자1("user1", "token1", "user1@gmail.com", "password1", 25),
    사용자2("user2", "token2", "user2@gmail.com", "password2", 26),
    사용자3("user3", "token3", "user3@gmail.com", "password3", 27),
    사용자4("user4", "token4", "user4@gmail.com", "password4", 28),
    ;

    private final String code;
    private final String accessToken;
    private final String email;
    private final String password;
    private final Integer age;

    VirtualUsers(final String code, final String accessToken, final String email, final String password,
                 final Integer age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public static Optional<VirtualUsers> toVirtualUser(final GithubAccessTokenRequest request) {
        return getSatisfiedUserWith(user -> Objects.equals(user.code, request.getCode()));
    }

    public static Optional<VirtualUsers> toVirtualUser(final String accessToken) {
        return getSatisfiedUserWith(user -> Objects.equals("token " + user.accessToken, accessToken));
    }

    private static Optional<VirtualUsers> getSatisfiedUserWith(final Predicate<VirtualUsers>predicate) {
        return Arrays.stream(values())
                .filter(predicate)
                .findAny();
    }

    public Member toBasicMember() {
        return Member.basic(email, password, age);
    }

    public GithubAccessTokenResponse toAccessTokenResponse() {
        return new GithubAccessTokenResponse(accessToken, "", "", "");
    }

    public GithubProfileResponse toProfileResponse() {
        return new GithubProfileResponse(email, 25);
    }
}
