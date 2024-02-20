package nextstep.auth.acceptance;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthSteps {
    private static String SECRET_KEY = "atdd-secret-key";
    private static long VALIDITYINMILLISECONDS = 3600000;

    private AuthSteps() {

    }
    public static ExtractableResponse<Response> 이메일_패스워드로_로그인을_요청한다(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
        return response;
    }

    public static ExtractableResponse<Response> 코드로_깃허브를_통한_로그인을_요청한다(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/github")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
        return response;
    }

    public static String createToken(final String email) {
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
