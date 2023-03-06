package nextstep.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.acceptance.support.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static nextstep.acceptance.support.AuthSteps.사용자_인증에_실패한다;
import static nextstep.acceptance.support.FavoriteSteps.로그인_된_상태에서_즐겨찾기_등록_요청;
import static nextstep.acceptance.support.FavoriteSteps.로그인_된_상태에서_즐겨찾기_목록_조회_요청;
import static nextstep.acceptance.support.FavoriteSteps.로그인_된_상태에서_즐겨찾기_삭제_요청;
import static nextstep.acceptance.support.FavoriteSteps.비로그인_상태에서_즐겨찾기_등록_요청;
import static nextstep.acceptance.support.FavoriteSteps.비로그인_상태에서_즐겨찾기_삭제_요청;
import static nextstep.acceptance.support.FavoriteSteps.비로그인_상태에서_즐겨찾기_조회_요청;
import static nextstep.acceptance.support.FavoriteSteps.즐겨찾기_등록_결과_Location;
import static nextstep.acceptance.support.FavoriteSteps.즐겨찾기_등록에_성공한다;
import static nextstep.acceptance.support.FavoriteSteps.즐겨찾기_목록_정보_조회됨;
import static nextstep.acceptance.support.FavoriteSteps.즐겨찾기_목록_조회에_성공한다;
import static nextstep.acceptance.support.FavoriteSteps.즐겨찾기_목록이_비어있음;
import static nextstep.acceptance.support.FavoriteSteps.즐겨찾기_목록이_한개_조회됨;
import static nextstep.acceptance.support.FavoriteSteps.즐겨찾기_삭제에_성공한다;
import static nextstep.acceptance.support.StationSteps.지하철역_생성_요청;
import static nextstep.fixture.AuthFixture.알렉스;
import static nextstep.fixture.FieldFixture.식별자_아이디;
import static nextstep.fixture.StationFixture.교대역;
import static nextstep.fixture.StationFixture.양재역;
import static nextstep.utils.JsonPathUtil.Long로_추출;

@DisplayName("즐겨찾기 기능 인수 테스트")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 양재역_id;
    private Long 교대역_id;

    @BeforeEach
    void setUpFixture() {
        양재역_id = Long로_추출(지하철역_생성_요청(양재역.역_이름()), 식별자_아이디);
        교대역_id = Long로_추출(지하철역_생성_요청(교대역.역_이름()), 식별자_아이디);
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 즐겨찾기_등록 {

        @Nested
        @DisplayName("로그인 된 상태에서 즐겨찾기를 등록하면")
        class Context_with_register_favorite_while_login {

            @Test
            @DisplayName("해당 사용자의 즐겨찾기 목록에 추가된다")
            void it_registered_favorite() throws Exception {
                ExtractableResponse<Response> 즐겨찾기_등록_결과 = 로그인_된_상태에서_즐겨찾기_등록_요청(알렉스, 양재역_id, 교대역_id);

                즐겨찾기_등록에_성공한다(즐겨찾기_등록_결과);
                즐겨찾기_목록이_한개_조회됨(로그인_된_상태에서_즐겨찾기_목록_조회_요청(알렉스));
            }
        }

        @Nested
        @DisplayName("비로그인 상태에서 즐겨찾기를 등록하면")
        class Context_with_register_favorite_while_not_login {

            @Test
            @DisplayName("401 Unauthorized 에러 코드를 응답한다")
            void it_responses_401() throws Exception {
                ExtractableResponse<Response> 즐겨찾기_등록_결과 = 비로그인_상태에서_즐겨찾기_등록_요청(양재역_id, 교대역_id);

                사용자_인증에_실패한다(즐겨찾기_등록_결과);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 즐겨찾기_조회 {

        @Nested
        @DisplayName("로그인 된 상태에서 즐겨찾기를 조회하면")
        class Context_with_select_favorite_while_login {

            @BeforeEach
            void setUp() {
                로그인_된_상태에서_즐겨찾기_등록_요청(알렉스, 양재역_id, 교대역_id);
            }

            @Test
            @DisplayName("해당 사용자의 즐겨찾기 목록이 조회된다")
            void it_returns_favorite_list() throws Exception {
                ExtractableResponse<Response> 즐겨찾기_조회_결과 = 로그인_된_상태에서_즐겨찾기_목록_조회_요청(알렉스);

                즐겨찾기_목록_조회에_성공한다(즐겨찾기_조회_결과);
                즐겨찾기_목록_정보_조회됨(즐겨찾기_조회_결과, 양재역, 교대역);
            }
        }

        @Nested
        @DisplayName("비로그인 상태에서 즐겨찾기를 조회하면")
        class Context_with_select_favorite_while_not_login {

            @Test
            @DisplayName("401 Unauthorized 에러 코드를 응답한다")
            void it_responses_401() throws Exception {
                ExtractableResponse<Response> 즐겨찾기_조회_결과 = 비로그인_상태에서_즐겨찾기_조회_요청();

                사용자_인증에_실패한다(즐겨찾기_조회_결과);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 즐겨찾기_삭제 {

        private String 등록된_즐겨찾기_Location;

        @BeforeEach
        void setUp() {
            등록된_즐겨찾기_Location = 즐겨찾기_등록_결과_Location(로그인_된_상태에서_즐겨찾기_등록_요청(알렉스, 양재역_id, 교대역_id));
        }

        @Nested
        @DisplayName("로그인 된 상태에서 즐겨찾기를 삭제하면")
        class Context_with_delete_favorite_while_login {

            @Test
            @DisplayName("해당 사용자의 즐겨찾기 목록에서 삭제된다")
            void it_deleted_favorite() throws Exception {
                ExtractableResponse<Response> 즐겨찾기_삭제_결과 = 로그인_된_상태에서_즐겨찾기_삭제_요청(알렉스, 등록된_즐겨찾기_Location);

                즐겨찾기_삭제에_성공한다(즐겨찾기_삭제_결과);
                즐겨찾기_목록이_비어있음(로그인_된_상태에서_즐겨찾기_목록_조회_요청(알렉스));
            }
        }

        @Nested
        @DisplayName("비로그인 상태에서 즐겨찾기를 삭제하면")
        class Context_with_delete_favorite_while_not_login {

            @Test
            @DisplayName("401 Unauthorized 에러 코드를 응답한다")
            void it_responses_401() throws Exception {
                ExtractableResponse<Response> 즐겨찾기_삭제_결과 = 비로그인_상태에서_즐겨찾기_삭제_요청(등록된_즐겨찾기_Location);

                사용자_인증에_실패한다(즐겨찾기_삭제_결과);
            }
        }
    }
}
