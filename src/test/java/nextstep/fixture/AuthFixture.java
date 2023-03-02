package nextstep.fixture;


import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.acceptance.support.AuthSteps.베어러_인증_로그인_요청;
import static nextstep.fixture.FieldFixture.Access_Token;
import static nextstep.fixture.FieldFixture.권한_증서;
import static nextstep.fixture.GitHubProfileFixture.ADMIN_GITHUB;
import static nextstep.fixture.GitHubProfileFixture.ALEX_GITHUB;
import static nextstep.fixture.MemberFixture.관리자_ADMIN;
import static nextstep.fixture.MemberFixture.회원_ALEX;
import static nextstep.utils.JsonPathUtil.문자열로_추출;

public enum AuthFixture {
    알렉스(회원_ALEX, ALEX_GITHUB),
    어드민(관리자_ADMIN, ADMIN_GITHUB),
    ;

    private static final String AUTH_TYPE_OAUTH = "Bearer ";
    private final MemberFixture memberFixture;
    private final GitHubProfileFixture gitHubProfileFixture;

    AuthFixture(MemberFixture memberFixture, GitHubProfileFixture gitHubProfileFixture) {
        this.memberFixture = memberFixture;
        this.gitHubProfileFixture = gitHubProfileFixture;
    }

    public MemberFixture 회원_정보() {
        return memberFixture;
    }

    public GitHubProfileFixture Github_정보() {
        return gitHubProfileFixture;
    }


    public static RequestSpecification 로그인된_상태(AuthFixture 인증_주체) {
        ExtractableResponse<Response> 베어러_인증_로그인_결과 = 베어러_인증_로그인_요청(인증_주체);

        return RestAssured.requestSpecification = new RequestSpecBuilder()
                .setAccept(MediaType.APPLICATION_JSON_VALUE)
                .setContentType(MediaType.APPLICATION_JSON_VALUE)
                .addHeader(HttpHeaders.AUTHORIZATION, AUTH_TYPE_OAUTH + 문자열로_추출(베어러_인증_로그인_결과, Access_Token))
                .build();
    }

    public Map<String, String> Github_로그인_요청_데이터_생성() {
        Map<String, String> params = new HashMap<>();
        params.put(권한_증서.필드명(), Github_정보().권한_증서_코드());

        return params;
    }
}
