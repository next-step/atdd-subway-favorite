package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationServiceTest {

    @Test
    void getLoginMember() {
        // given
        LoginMember loginMember = new LoginMember(1L, "123@naver.com", "123", 10);
        Authentication authentication = new Authentication(loginMember);
        AuthenticationService authenticationService = new AuthenticationService();
        SecurityContextHolder.setContext(new SecurityContext(authentication));

        // when
        Optional<UserDetails> loginMemberOptional = authenticationService.getLoginMember();

        // then
        assertThat(loginMemberOptional).isNotEmpty();
        assertThat(loginMemberOptional.get()).isEqualTo(loginMember);
    }
}