package nextstep.member.utils.steps;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.dto.MemberRequest;
import nextstep.common.RestApiRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberSteps {
    private static final String MEMBER_API_URL = "/members";
    private static final RestApiRequest<MemberRequest> apiRequest = new RestApiRequest<>();

    public static ExtractableResponse<Response> 회원_생성_요청(String email, String password, Integer age) {
        return apiRequest.post(MEMBER_API_URL, new MemberRequest(email, password, age));
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return apiRequest.get(uri);
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email, String password, Integer age) {
        String uri = response.header("Location");

        return apiRequest.put(uri, new MemberRequest(email, password, age));
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return apiRequest.delete(uri);
    }

    public static ExtractableResponse<Response> 내_정보_조회(String accessToken) {
        return apiRequest.get(MEMBER_API_URL + "/me", accessToken);
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        assertThat(response.jsonPath().getString("id")).isNotNull();
        assertThat(response.jsonPath().getString("email")).isEqualTo(email);
        assertThat(response.jsonPath().getInt("age")).isEqualTo(age);
    }
}
