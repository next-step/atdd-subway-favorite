package nextstep.favorite.acceptance;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {

    public static final String FAVORITES_URL = "/favorites";
    private static String SECRET_KEY = "atdd-secret-key";
    private static long VALIDITYINMILLISECONDS = 3600000;

    private FavoriteSteps() {

    }

    public static ExtractableResponse<Response> 즐겨찾기를_등록한다(final Long source, final Long target) {
        Map<String, String> params = createParams(source, target);
        return RestAssured
                .given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(FAVORITES_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 토큰을_포함하여_즐겨찾기를_등록한다(final String email,final Long source, final Long target) {
        final String jwtToken = createToken(email);
        Map<String, String> params = createParams(source, target);

        return RestAssured.given().log().all()
                .auth().oauth2(jwtToken)
                .body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(FAVORITES_URL)
                .then().log().all()
                .extract();
    }

    private static Map<String, String> createParams(final Long source, final Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(source));
        params.put("target", String.valueOf(target));
        return params;
    }

    private static String createToken(final String email) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDITYINMILLISECONDS);

        final String jwtToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        return jwtToken;
    }

}
