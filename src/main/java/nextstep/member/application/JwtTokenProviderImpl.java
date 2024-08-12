package nextstep.member.application;

import io.jsonwebtoken.*;
import nextstep.member.domain.LoginMember;
import nextstep.security.application.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider<LoginMember> {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private Long validityInMilliseconds;

    public JwtTokenProviderImpl() {
    }

    public JwtTokenProviderImpl(final String secretKey, final long validityInMilliseconds) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    @Override
    public String createToken(LoginMember loginMember) {
        Claims claims = Jwts.claims().setSubject(loginMember.getEmail());
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .claim("id", loginMember.getId())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    @Override
    public LoginMember parseToken(String token) {
        Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return new LoginMember(
                Long.parseLong(body.get("id").toString()),
                body.getSubject()
        );
    }

}

