package atdd.path.security;

import atdd.path.SoftAssertionTest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static atdd.path.dao.UserDao.EMAIL_KEY;
import static atdd.path.fixture.UserFixture.KIM_EMAIL;
import static atdd.path.security.TokenAuthenticationService.BEARER_TOKEN_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

public class TokenAuthenticationServiceTest extends SoftAssertionTest {
    private static final String SALT = "63B75D39E3F6BFE72263F7C1145AC22E";

    private TokenAuthenticationService tokenAuthenticationService;

    @BeforeEach
    void setUp() {
        this.tokenAuthenticationService = new TokenAuthenticationService();
    }

    @DisplayName("JWT 를 위한 키가 생성되는지")
    @Test
    public void generateKey() {
        //given
        byte[] key = tokenAuthenticationService.generateKey(SALT);

        //when

        //then
        assertThat(key).isEqualTo(SALT.getBytes());
    }

    @DisplayName("Jwt Token 을 생성하는지")
    @Test
    public void toJwtByEmail() {
        //when
        String jwt = tokenAuthenticationService.toJwtByEmail(KIM_EMAIL);

        //then
        assertThat(jwt).isNotEmpty();
    }

    @DisplayName("JWT 에서 Claim 을 가져오는지")
    @Test
    public void getJwtClaim() {
        String jwt = tokenAuthenticationService.toJwtByEmail(KIM_EMAIL);

        //when
        Jws<Claims> claims = tokenAuthenticationService.getJwtClaim(jwt);

        //then
        assertThat(claims.getBody().get(EMAIL_KEY)).isEqualTo(KIM_EMAIL);
    }

    @DisplayName("Claims 에서 email 을 가져오는지")
    @Test
    public void getEmailByClaim() {
        String jwt = tokenAuthenticationService.toJwtByEmail(KIM_EMAIL);
        Jws<Claims> claims = tokenAuthenticationService.getJwtClaim(jwt);

        //when
        String email = tokenAuthenticationService.getEmailByClaims(claims);

        //then
        assertThat(email).isEqualTo(KIM_EMAIL);
    }

    @DisplayName("Claims 에서 tokenType 을 가져오는지")
    @Test
    public void getTokenType() {
        String jwt = tokenAuthenticationService.toJwtByEmail(KIM_EMAIL);
        Jws<Claims> claims = tokenAuthenticationService.getJwtClaim(jwt);

        //when
        String tokenType = tokenAuthenticationService.getTokenType(claims);

        //then
        assertThat(tokenType).isEqualTo(BEARER_TOKEN_TYPE);
    }

}

