package nextstep.member.acceptance.step;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.token.dto.TokenResponse;
import nextstep.constants.Endpoint;
import nextstep.member.dto.MemberRequest;
import nextstep.member.dto.MemberResponse;
import nextstep.support.RestAssuredClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MemberStep {

    private static final String MEMBER_BASE_URL = Endpoint.MEMBER_BASE_URL.getUrl();

    /**
     * <pre>
     * 회원을 생성하는 API를 호출하는 함수
     * </pre>
     *
     * @param memberRequest 회원 생성 요청 DTO
     * @return ExtractableResponse
     */
    public static ExtractableResponse<Response> 회원_생성_요청(MemberRequest memberRequest) {
        return RestAssuredClient.post(MEMBER_BASE_URL, memberRequest);
    }

    /**
     * <pre>
     * 회원 정보를 조회하는 API를 호출하는 함수
     * </pre>
     *
     * @param response RestAssured를 통해 API 응답 받은 응답 객체
     * @return ExtractableResponse
     */
    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssuredClient.get(uri);
    }

    /**
     * <pre>
     * 회원 정보를 수정하는 API를 호출하는 함수
     * </pre>
     *
     * @param response RestAssured를 통해 API 응답 받은 응답 객체
     * @param memberRequest 회원 수정 요청 DTO
     * @return ExtractableResponse
     */
    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, MemberRequest memberRequest) {
        String uri = response.header("Location");

        return RestAssuredClient.put(uri, memberRequest);
    }

    /**
     * <pre>
     * 회원을 삭제하는 API를 호출하는 함수
     * </pre>
     *
     * @param response RestAssured를 통해 API 응답 받은 응답 객체
     * @return ExtractableResponse
     */
    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssuredClient.delete(uri);
    }

    /**
     * <pre>
     * 현재 로그인한 회원의 정보를 조회하는 API를 호출하는 함수
     * </pre>
     *
     * @param response RestAssured를 통해 로그인 API를 호출해서 응답 받은 응답 객체
     * @return ExtractableResponse
     */
    public static ExtractableResponse<Response> 내_정보_조회_요청(ExtractableResponse<Response> response) {
        TokenResponse tokenResponse = response.as(TokenResponse.class);
        String accessToken = tokenResponse.getAccessToken();
        String uri = String.format("%s/me", MEMBER_BASE_URL);

        return RestAssuredClient.get(uri, accessToken);
    }

    /**
     * <pre>
     * 회원 정보가 잘 조회되었는지 검증하는 함수
     * </pre>
     *
     * @param response RestAssured를 통해 회원 정보 조회 API 응답 받은 응답 객체
     * @param email 검증 기준이 될 이메일
     * @param age 검증 기준이 될 나이
     * @return ExtractableResponse
     */
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