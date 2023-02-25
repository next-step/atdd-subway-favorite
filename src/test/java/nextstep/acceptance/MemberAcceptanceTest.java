package nextstep.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.acceptance.support.AcceptanceTest;
import nextstep.fixture.AuthFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.acceptance.support.MemberSteps.내_정보_조회가_성공한다;
import static nextstep.acceptance.support.MemberSteps.베어러_인증으로_내_회원_정보_조회_요청;
import static nextstep.acceptance.support.MemberSteps.회원_삭제_요청;
import static nextstep.acceptance.support.MemberSteps.회원_생성_요청;
import static nextstep.acceptance.support.MemberSteps.회원_정보_수정_요청;
import static nextstep.acceptance.support.MemberSteps.회원_정보_조회_요청;
import static nextstep.acceptance.support.MemberSteps.회원_정보_조회됨;
import static nextstep.fixture.AuthFixture.알렉스;
import static nextstep.fixture.MemberFixture.회원_ALEX;
import static nextstep.fixture.MemberFixture.회원_ALEX_수정;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("회원 관리 기능")
class MemberAcceptanceTest extends AcceptanceTest {

    @Nested
    @DisplayName("회원 가입을 요청하면")
    class Context_with_create_member {

        @Test
        @DisplayName("회원이 등록된다")
        void it_success_created() throws Exception {
            ExtractableResponse<Response> 회원_생성_결과 = 회원_생성_요청(회원_ALEX.회원_등록_요청_데이터_생성());

            assertThat(회원_생성_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        }
    }


    @Nested
    @DisplayName("회원 조회를 요청하면")
    class Context_with_get_member {

        private String 회원_생성_결과_Location;

        @BeforeEach
        void setUp() {
            회원_생성_결과_Location = 회원_생성_요청(회원_ALEX.회원_등록_요청_데이터_생성()).header("Location");
        }

        @Test
        @DisplayName("등록되어 있는 회원 정보가 반환된다")
        void it_returns_member() throws Exception {
            ExtractableResponse<Response> 회원_조회_결과 = 회원_정보_조회_요청(회원_생성_결과_Location);

            회원_정보_조회됨(회원_조회_결과, 회원_ALEX);
        }
    }

    @Nested
    @DisplayName("회원 정보 수정을 요청하면")
    class Context_with_update_member {

        private String 회원_생성_결과_Location;

        @BeforeEach
        void setUp() {
            회원_생성_결과_Location = 회원_생성_요청(회원_ALEX.회원_등록_요청_데이터_생성()).header("Location");
        }

        @Test
        @DisplayName("회원이 수정된다")
        void it_success_updated() throws Exception {
            ExtractableResponse<Response> 회원_정보_수정_결과 = 회원_정보_수정_요청(회원_생성_결과_Location, 회원_ALEX_수정.회원_수정_요청_데이터_생성());

            assertThat(회원_정보_수정_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        }
    }

    @Nested
    @DisplayName("회원 정보 삭제를 요청하면")
    class Context_with_delete_member {

        private String 회원_생성_결과_Location;

        @BeforeEach
        void setUp() {
            회원_생성_결과_Location = 회원_생성_요청(회원_ALEX.회원_등록_요청_데이터_생성()).header("Location");
        }

        @Test
        @DisplayName("회원이 삭제된다")
        void it_success_deleted() throws Exception {
            ExtractableResponse<Response> 회원_삭제_결과 = 회원_삭제_요청(회원_생성_결과_Location);

            assertThat(회원_삭제_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }
    }

    @Nested
    @DisplayName("AccessToken을 Header에 포함하여 내 정보 조회를 요청하면")
    class Context_with_select_my_info_with_access_token {

        private final AuthFixture 인증_주체 = 알렉스;

        @Test
        @DisplayName("인증 주체의 회원 정보가 반환된다")
        void it_returns_member() throws Exception {
            ExtractableResponse<Response> 내_정보_조회_결과 = 베어러_인증으로_내_회원_정보_조회_요청(인증_주체);

            내_정보_조회가_성공한다(내_정보_조회_결과);
            회원_정보_조회됨(내_정보_조회_결과, 인증_주체.회원_정보());
        }
    }
}
