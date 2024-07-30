package nextstep.favorite.acceptance;

import io.restassured.response.Response;
import nextstep.favorite.payload.FavoriteResponse;
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
import static nextstep.utils.HttpStatusAssertion.assertCreated;
import static nextstep.utils.HttpStatusAssertion.assertNoContent;
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
        void test() {
            Response response = 즐겨찾기를_생성한다(joy, lineFixture.교대역(), lineFixture.양재역());
            assertCreated(response.statusCode());
        }

        //TODO 끊어진 경로인 경우 생성 불가

        //TODO source target 같으면 생성 불가

        //TODO 동일 경로인경우 생성 불가

        //TODO 로그인 유저가 아닌 경우 생성 불가
    }

    @DisplayName("즐겨 찾기를 조회 한다")
    @Nested
    class WhenShow {
        @Test
        void test() {
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
        void test() {
            var 즐겨찾기1 = 즐겨찾기_생성후_ID를_추출한다(joy, lineFixture.교대역(), lineFixture.양재역());
            var 즐겨찾기2 = 즐겨찾기_생성후_ID를_추출한다(joy, lineFixture.강남역(), lineFixture.남부터미널역());

            Response 삭제_결과 = 즐겨찾기를_삭제한다(joy,즐겨찾기2);
            assertNoContent(삭제_결과.statusCode());

            var 조회_결과 = 특정회원의_즐겨찾기를_전체_조회한다(joy).jsonPath().getList("" , FavoriteResponse.class);

            assertAll(() -> assertThat(조회_결과).hasSize(1),
                    () ->assertThat(조회_결과.get(0).getSource().getId()).isEqualTo(lineFixture.교대역()),
                    () ->assertThat(조회_결과.get(0).getTarget().getId()).isEqualTo(lineFixture.양재역())
            );
        }
    }



}
