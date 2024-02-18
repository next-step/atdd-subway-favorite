package nextstep.core;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.auth.acceptance.AuthSteps.로그인_요청;

@AcceptanceTest
public abstract class AcceptanceTestAuthBase {

    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    protected String accessToken;

    @Autowired
    private MemberRepository memberRepository;


    @BeforeEach
    void acceptanceTestAuthBaseSetUp() {
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
        final ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);
        accessToken = response.jsonPath().getString("accessToken");
    }
}
