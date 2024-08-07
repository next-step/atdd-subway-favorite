package nextstep.member.acceptance;

import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.member.acceptance.MemberApiRequest.내_정보를_조회한다;
import static nextstep.member.acceptance.TokenApiRequest.토큰을_발급한다;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        Response response = 토큰을_발급한다(EMAIL, PASSWORD);
        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();

        var 내정보 = 내_정보를_조회한다(accessToken);

        assertThat(내정보.jsonPath().getString("email")).isEqualTo(EMAIL);
    }
}
