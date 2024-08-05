package nextstep.member.application.dto;

import nextstep.member.AccessTokenException;
import nextstep.member.test.GithubUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GithubUserTest {

    @DisplayName("인증코드가 올바르지 않다면 에러가 발생합니다.")
    @Test
    void invalidCode() {
        // given
        String code = "asdfasdf";
        // when
        String token = GithubUser.getTokenByCode(code);
        // then
        Assertions.assertThat(token).isBlank();
    }

    @DisplayName("인증코드에 맞는 사용자 accessToken 을 반환합니다.")
    @Test
    void validCode() {
        // given
        // when
        String result = GithubUser.getTokenByCode(GithubUser.사용자1.getCode());
        // then
        Assertions.assertThat(result).isEqualTo(GithubUser.사용자1.getAccessToken());
    }

}