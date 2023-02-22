package nextstep.member.unit;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.DataLoader.ADMIN_EMAIL;
import static nextstep.DataLoader.ADMIN_ROLES;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenProviderTest {

    private static final String TEST_SECRET = "test_secret";
    private static final int VALIDITY_IN_MILLISECONDS = 36000;
    private static final int INVALIDITY_IN_MILLISECONDS = -36000;

    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(TEST_SECRET, VALIDITY_IN_MILLISECONDS);

    private String validToken;
    private String inValidToken;

    @BeforeEach
    void setUp() {
        validToken = jwtTokenProvider.createToken(ADMIN_EMAIL, ADMIN_ROLES);
        inValidToken = new JwtTokenProvider(TEST_SECRET, INVALIDITY_IN_MILLISECONDS).createToken(ADMIN_EMAIL, ADMIN_ROLES);
    }

    @Test
    void getRoles() {
        List<RoleType> roles = jwtTokenProvider.getRoles(validToken);

        assertTrue(roles.containsAll(ADMIN_ROLES));
    }

    @Test
    void validate() {
        assertTrue(jwtTokenProvider.validateToken(validToken));
    }

    @Test
    void inValidate() {
        assertFalse(jwtTokenProvider.validateToken(inValidToken));
    }
}
