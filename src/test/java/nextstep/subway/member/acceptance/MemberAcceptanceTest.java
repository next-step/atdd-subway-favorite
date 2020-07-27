package nextstep.subway.member.acceptance;

import static nextstep.subway.member.acceptance.step.MemberAcceptanceStep.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        ExtractableResponse<Response> response = 회원_생성을_요청(EMAIL, PASSWORD, AGE);

        // then
        회원_생성됨(response);
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        회원_정보_수정됨(response);
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);

        // then
        회원_삭제됨(response);
    }

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when: 회원 생성을 요청한다.
        ExtractableResponse<Response> 회원생성_요청 = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then: 회원이 생성된다.
        회원_생성됨(회원생성_요청);

        // when: 회원 정보 조회를 요청한다.
        ExtractableResponse<Response> 회원정보_조회요청 = 회원_정보_조회_요청(회원생성_요청);
        // then: 회원 정보가 조회된다.
        회원_정보_조회됨(회원정보_조회요청, EMAIL, AGE);

        // when: 회원 정보 수정을 요청한다.
        ExtractableResponse<Response> 회원수정_요청 = 회원_정보_수정_요청(회원생성_요청, EMAIL, PASSWORD, AGE);
        // then: 회원 정보가 수정된다.
        회원_정보_수정됨(회원수정_요청);

        // when: 회원 삭제가 요청된다.
        ExtractableResponse<Response> 회원삭제_요청 = 회원_삭제_요청(회원생성_요청);
        // then: 회원 정보가 삭제된다.
        회원_삭제됨(회원삭제_요청);
    }

    @DisplayName("내 회원 정보를 수정한다.")
    @Test
    void editMyMemberInformation() {
        // given: 회원 정보가 등록되어 있으며, 로그인이 되어 있다.
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);
        로그인_요청(EMAIL, PASSWORD);

        // when: 회원 정보 수정을 요청한다.
        ExtractableResponse<Response> response = 내_회원_정보_수정_요청(tokenResponse, EMAIL, PASSWORD, AGE);

        // then: 회원 정보 수정이 되었는지 확인한다.
        회원_정보_수정됨(response);
    }

    @DisplayName("내 회원 정보를 삭제한다.")
    @Test
    void deleteMyMemberInformation() {
        // given: 회원 정보가 등록되어 있으며, 로그인이 되어 있다.
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);
        로그인_요청(EMAIL, PASSWORD);

        // when: 회원 정보 삭제를 요청한다.
        ExtractableResponse<Response> response = 내_회원_정보_삭제_요청(tokenResponse);

        // then: 회원 정보 삭제가 되었는지 확인한다.
        회원_삭제됨(response);
    }
}
