package nextstep.core;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

@AcceptanceTest
public abstract class AcceptanceTestAuthBase {

    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    protected String accessToken;

    @Autowired
    private MemberRepository memberRepository;


    @BeforeEach
    void setUp() {
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of("email", EMAIL, "password", PASSWORD))
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
        accessToken = response.jsonPath().getString("accessToken");
    }
}
