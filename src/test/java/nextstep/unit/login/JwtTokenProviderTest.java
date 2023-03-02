package nextstep.unit.login;

import nextstep.fixture.MemberFixture;
import nextstep.login.infra.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static nextstep.fixture.MemberFixture.회원_ALEX;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JWT Token 생성 테스트")
class JwtTokenProviderTest {

    private static final String SECRET_KEY = "atdd-secret-key";

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 토큰_생성 {

        private final MemberFixture 인증_주체 = 회원_ALEX;
        private JwtTokenProvider 토큰_생성기 = new JwtTokenProvider(SECRET_KEY, 3600000);
        private String accessToken;


        @BeforeEach
        void setUp() {
            accessToken = 토큰_생성기.createToken(인증_주체.이메일(), 인증_주체.역할_목록());
        }

        @Nested
        @DisplayName("토큰을 생성하면")
        class Context_with_create_token {

            @Test
            @DisplayName("토큰 검증 시 유효하다")
            void it_valid_access_token() throws Exception {
                assertThat(토큰_생성기.validateToken(accessToken)).isTrue();
            }
        }

        @Nested
        @DisplayName("토큰의 인증 주체를 조회하면")
        class Context_with_get_principal {

            @Test
            @DisplayName("인증 주체의 이메일을 반환한다")
            void it_returns_email() throws Exception {
                assertThat(토큰_생성기.getPrincipal(accessToken)).isEqualTo(인증_주체.이메일());
            }
        }

        @Nested
        @DisplayName("토큰의 권한을 조회하면")
        class Context_with_get_roles {

            @Test
            @DisplayName("갖고 있는 권한 목록을 반환한다")
            void it_returns_role_list() throws Exception {
                assertThat(토큰_생성기.getRoles(accessToken)).containsAll(인증_주체.역할_목록());
            }
        }

        @Nested
        @DisplayName("토큰을 디코딩하면")
        class Context_with_decode_token {

            @Test
            @DisplayName("인증 주체의 정보를 반환한다")
            void it_returns_member_payload() throws Exception {
                assertThat(토큰_생성기.decodeToken(accessToken).getEmail()).isEqualTo(인증_주체.이메일());
                assertThat(토큰_생성기.decodeToken(accessToken).getRoles()).containsAll(인증_주체.역할_목록());
            }
        }
    }
}
