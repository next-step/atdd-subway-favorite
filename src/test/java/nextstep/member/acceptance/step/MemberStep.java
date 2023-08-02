package nextstep.member.acceptance.step;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.constants.Endpoint;
import nextstep.member.dto.MemberRequest;
import nextstep.member.dto.MemberResponse;
import nextstep.support.RestAssuredClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MemberStep {

    private static final String MEMBER_BASE_URL = Endpoint.MEMBER_BASE_URL.getUrl();

    public static ExtractableResponse<Response> 회원_생성_요청(MemberRequest memberRequest) {
        return RestAssuredClient.post(MEMBER_BASE_URL, memberRequest);
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssuredClient.get(uri);
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, MemberRequest memberRequest) {
        String uri = response.header("Location");

        return RestAssuredClient.put(uri, memberRequest);
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssuredClient.delete(uri);
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse 회원_정보_조회_응답 = response.as(MemberResponse.class);

        Long 아이디 = 회원_정보_조회_응답.getId();
        String 이메일 = 회원_정보_조회_응답.getEmail();
        Integer 나이 = 회원_정보_조회_응답.getAge();

        assertAll(
                () -> assertThat(아이디).isNotNull(),
                () -> assertThat(이메일).isEqualTo(email),
                () -> assertThat(나이).isEqualTo(age)
        );
    }
}