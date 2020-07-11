package nextstep.subway.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.member.acceptance.step.MemberAcceptanceStep.*;

public class MemberAcceptanceTest extends AcceptanceTest {
    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        ExtractableResponse<Response> response = 회원_생성을_요청("email@email.com", "password", 20);

        // then
        회원_생성됨(response);
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        회원_등록되어_있음("email@email.com", "password", 20);

        // when
        ExtractableResponse<Response> response = 회원_정보_조회_요청();

        // then
        회원_정보_조회됨(response);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        회원_등록되어_있음("email@email.com", "password", 20);

        // when
        ExtractableResponse<Response> response = 회원_정보_수정_요청("newemail@email.com", "newpassword", 20);

        // then
        회원_정보_수정됨(response);
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        회원_등록되어_있음("email@email.com", "password", 20);

        // when
        ExtractableResponse<Response> response = 회원_삭제_요청();

        // then
        회원_삭제됨(response);
    }
}
