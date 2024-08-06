package nextstep.member.infrastructure.jwt;

import autoparams.AutoSource;
import nextstep.auth.LoginMember;
import nextstep.member.domain.entity.TokenPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {
    @DisplayName("createToken")
    @Nested
    class CreateToken {
        @ParameterizedTest
        @AutoSource
        public void sut_returns_token(JwtTokenProvider sut, TokenPrincipal principal) {
            // when
            String actual = sut.createToken(principal);

            // then
            assertThat(sut.validateToken(actual)).isEqualTo(true);
        }
    }

    @DisplayName("getPrincipal")
    @Nested
    class GetPrincipal {
        @ParameterizedTest
        @AutoSource
        public void sut_extract_principal(JwtTokenProvider sut, TokenPrincipal expected) {
            // given
            String token = sut.createToken(expected);

            // when
            TokenPrincipal actual = sut.getPrincipal(token);

            // then
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }
    }

    @DisplayName("authorize")
    @Nested
    class Authorize {
        @ParameterizedTest
        @AutoSource
        public void sut_returns_login_member(JwtTokenProvider sut, TokenPrincipal expected) {
            // given
            String token = sut.createToken(expected);

            // when
            LoginMember actual = sut.authorize(token);

            // then
            assertThat(actual.getId()).isEqualTo(expected.getSubject());
            assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        }
    }
}