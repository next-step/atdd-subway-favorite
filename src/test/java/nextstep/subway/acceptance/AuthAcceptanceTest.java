package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.DataLoader;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.subway.acceptance.MemberSteps.*;


class AuthAcceptanceTest extends AcceptanceTest {

    private static final Member ADMIN = Member.createAdmin("parkuram12@gmail.com", "pass", 25);
    private static final Member USER = Member.createUser("user2@gmail.com", "user3", 23);

    @Autowired
    DataLoader dataLoader;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        dataLoader.loadData();
    }

    @DisplayName("Basic Auth")
    @Test
    void myInfoWithBasicAuth() {
        ExtractableResponse<Response> response = 베이직_인증으로_내_회원_정보_조회_요청(ADMIN.getEmail(), ADMIN.getPassword());

        회원_정보_조회됨(response, ADMIN.getEmail(), ADMIN.getAge());
    }

    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        ExtractableResponse<Response> response = 폼_로그인_후_내_회원_정보_조회_요청(USER.getEmail(), USER.getPassword());

        회원_정보_조회됨(response, USER.getEmail(), USER.getAge());
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        String accessToken = 로그인_되어_있음(USER.getEmail(), USER.getPassword());

        ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(accessToken);

        회원_정보_조회됨(response, USER.getEmail(), USER.getAge());
    }


}
