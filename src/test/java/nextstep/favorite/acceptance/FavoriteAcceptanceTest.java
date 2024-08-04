package nextstep.favorite.acceptance;

import io.restassured.response.Response;
import nextstep.line.fixture.LineFixture;
import nextstep.line.fixture.LineFixtureGenerator;
import nextstep.member.fixture.AuthFixture;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.favorite.acceptance.FavoriteApiRequest.*;
import static nextstep.utils.HttpStatusAssertion.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    @Autowired
    private AuthFixture authFixture;

    @Autowired
    private LineFixtureGenerator lineFixtureGenerator;
    private LineFixture lineFixture;
    private String joy;
    private String jennie;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        lineFixture = lineFixtureGenerator.createLineFixture();
        joy = authFixture.getMemberAccessToken("joy@email.com");
        jennie = authFixture.getMemberAccessToken("jannie@email.com");
    }

    @DisplayName("즐겨 찾기 생성")
    @Nested
    class WhenAdd {
        @DisplayName("즐겨 찾기를 생성한다")
        @Test
        void whenAddFavorite() {
            Response response = 즐겨찾기를_생성한다(joy, lineFixture.교대역(), lineFixture.양재역());
            assertCreated(response.statusCode());
        }

        @DisplayName("끊어진 경로인 경우 생성되지 않는다")
        @Test
        void whenBrokenPathThenReturn400() {
            // When 끊어진 경로를 등록하려는 경우
            Response response = 즐겨찾기를_생성한다(joy, lineFixture.교대역(), lineFixture.부산역());
            // Then 400을 반환한다
            assertBadRequest(response.statusCode());
        }

        @DisplayName("target 과 source가 같은경우 생성되지 않는다")
        @Test
        void whenSourceAndTargetSame() {
            Response response = 즐겨찾기를_생성한다(joy, lineFixture.교대역(), lineFixture.교대역());
            assertBadRequest(response.statusCode());
        }

    }

    @DisplayName("즐겨 찾기를 조회 한다")
    @Nested
    class WhenShow {
        @DisplayName("내 즐겨찾기를 모두 조회한다")
        @Test
        void whenShowMyAllFavorites() {
            즐겨찾기를_생성한다(joy, lineFixture.교대역(), lineFixture.양재역());
            즐겨찾기를_생성한다(joy, lineFixture.강남역(), lineFixture.남부터미널역());
            var 조회_결과 = 특정회원의_즐겨찾기를_전체_조회한다(joy).jsonPath();

            assertThat(조회_결과.getLong("[0].source.id")).isEqualTo(lineFixture.교대역());
            assertThat(조회_결과.getLong("[0].target.id")).isEqualTo(lineFixture.양재역());
            assertThat(조회_결과.getLong("[1].source.id")).isEqualTo(lineFixture.강남역());
            assertThat(조회_결과.getLong("[1].target.id")).isEqualTo(lineFixture.남부터미널역());

        }


    }

    @DisplayName("즐겨 찾기 삭제")
    @Nested
    class WhenDelete {


        @DisplayName("즐겨찾기 삭제 후 재 조회시 조회되지 않는다")
        @Test
        void whenDeleteMyAllFavorite() {
            즐겨찾기_생성후_ID를_추출한다(joy, lineFixture.교대역(), lineFixture.양재역());
            var 즐겨찾기2 = 즐겨찾기_생성후_ID를_추출한다(joy, lineFixture.강남역(), lineFixture.남부터미널역());

            // When 즐겨찾기 삭제후 재조회시
            Response 삭제_결과 = 즐겨찾기를_삭제한다(joy, 즐겨찾기2);
            assertNoContent(삭제_결과.statusCode());
            var 조회_결과 = 특정회원의_즐겨찾기를_전체_조회한다(joy).jsonPath();
            
            //Then 조회되지 않는다
            assertAll(
                    () -> assertThat(조회_결과.getList("source.name", String.class)).containsExactly("교대역"),
                    () -> assertThat(조회_결과.getList("target.name", String.class)).containsExactly("양재역")
            );
        }

        @DisplayName("다른 사람의 즐겨찾기를 삭제하려는 경우 403 인가 에러를 반환한다")
        @Test
        void whenDeleteOtherFavorite() {
            var 즐겨찾기 = 즐겨찾기_생성후_ID를_추출한다(joy, lineFixture.교대역(), lineFixture.양재역());

            //When 다른사람의 즐겨찾기를 삭제하려는 경우
            Response 삭제_결과 = 즐겨찾기를_삭제한다(jennie, 즐겨찾기);

            //Then 403 인가 에러를 반환한다
            assertForbidden(삭제_결과.statusCode());
        }
    }


}
