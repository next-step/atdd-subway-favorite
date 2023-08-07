package nextstep.member.acceptance;

import nextstep.member.application.dto.MemberResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.member.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        회원_생성_요청_성공(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        String 회원_URL = 회원_생성_요청_성공(EMAIL, PASSWORD, AGE);

        // when
        MemberResponse response = 회원_정보_조회_요청_성공(회원_URL);

        // then
        assertThat(response).isNotNull();
    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        String 회원_URL = 회원_생성_요청_성공(EMAIL, PASSWORD, AGE);

        // when, then
        회원_정보_수정_요청_성공(회원_URL, "new" + EMAIL, "new" + PASSWORD, AGE);
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        String 회원_URL = 회원_생성_요청_성공(EMAIL, PASSWORD, AGE);

        // when, then
        회원_삭제_요청_성공(회원_URL);
    }

    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        // given
        회원_생성_요청_성공(EMAIL, PASSWORD, AGE);
        String accessToken = 토큰_기반_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

        // when
        MemberResponse memberResponse = 내_정보_조회_요청_성공(accessToken);

        // then
        assertThat(memberResponse).isNotNull();
    }
}