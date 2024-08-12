package nextstep.auth.acceptance;

import nextstep.member.domain.Member;
import nextstep.auth.fake.GithubUsers;
import nextstep.member.infrastructure.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static nextstep.auth.acceptance.AuthSteps.Github_로그인_토큰_요청;
import static nextstep.auth.acceptance.AuthSteps.로그인_토큰_요청;
import static nextstep.member.acceptance.MemberSteps.본인_정보_조회;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인증 토큰 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = "classpath:truncate-tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AuthAcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Bearer Auth")
    void bearerAuth() {
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
        var response = 로그인_토큰_요청(EMAIL, PASSWORD);
        var accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();

        var response2 = 본인_정보_조회(accessToken);
        assertThat(response2.jsonPath().getString("email")).isEqualTo(EMAIL);
    }


    /**
     * Given 회원 가입을 사용자가
     * When github 를 통해 인증을 요청하면
     * Then 엑세스 토큰이 발행된다
     */
    @Test
    @DisplayName("회원 가입을 한 사용자는 github 를 통해 엑세스 토큰을 발행할 수 있다.")
    void githubAuthRequestFromMember() {
        // when
        var 사용자1 = GithubUsers.사용자1;
        memberRepository.save(new Member(사용자1.getEmail(), PASSWORD, AGE));

        // when
        var response = Github_로그인_토큰_요청(사용자1.getCode());

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * Given 회원 가입을 하지 않은 사용자가
     * When github 를 통해 인증을 요청하면
     * Then 엑세스 토큰이 발행된다
     */
    @Test
    @DisplayName("회원 가입 하지 않은 사용자는 github 를 통해 가입과 동시에 엑세스 토큰을 발행할 수 있다.")
    void githubAuthRequestFromNonMember() {
        // given
        var 사용자1 = GithubUsers.사용자1;
        assertThat(memberRepository.findByEmail(사용자1.getEmail())).isEmpty();


        // when
        var response = Github_로그인_토큰_요청(사용자1.getCode());

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}
