package nextstep.subway.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.*;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.BearerTokenAuthenticationFilter;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class BearerTokenAuthenticationFilterTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static String JWT_TOKEN = null;


    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @InjectMocks
    private BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter;

    @Mock
    private JwtTokenProvider jwtTokenProvider;


    @BeforeEach
    void setUp() throws JsonProcessingException {
        jwtTokenProvider = new JwtTokenProvider();
        jwtTokenProvider.setSecretKey("atdd-secret-key");
        jwtTokenProvider.setValidityInMilliseconds(3600000);
        bearerTokenAuthenticationFilter = new BearerTokenAuthenticationFilter(jwtTokenProvider);

        JWT_TOKEN = createToken(EMAIL, List.of(RoleType.ROLE_MEMBER.name()));
        request = createMockRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void tokenValidation() {
        boolean result = validateToken(JWT_TOKEN);

        assertThat(result).isEqualTo(result);
    }

    @Test
    void convert() {
        //when
        AuthenticationToken token = bearerTokenAuthenticationFilter.convert(request);

        //then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(JWT_TOKEN);

    }

    @Test
    void authenticate() {
        //when
        AuthenticationToken token = new AuthenticationToken(EMAIL, JWT_TOKEN);
        Authentication authentication = bearerTokenAuthenticationFilter.authenticate(token);

        //then
        assertThat(authentication.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authentication.getAuthorities()).isEqualTo(List.of(RoleType.ROLE_MEMBER.name()));

    }

    @Test
    void preHandle() {

        //when
        boolean result = bearerTokenAuthenticationFilter.preHandle(request, response, null);

        //then
        assertThat(result).isTrue();


    }

    private MockHttpServletRequest createMockRequest() throws JsonProcessingException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "bearer " + JWT_TOKEN);
        return request;
    }

    public String createToken(String principal, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(principal);
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS256, "atdd-secret-key")
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey("atdd-secret-key").parseClaimsJws(token);
            System.out.println("claims : " + claims);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
