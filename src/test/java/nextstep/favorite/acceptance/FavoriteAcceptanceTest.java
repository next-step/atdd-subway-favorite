package nextstep.favorite.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.acceptance.AuthStep;
import nextstep.member.acceptance.MemberSteps;
import nextstep.subway.acceptance.LineSteps;
import nextstep.subway.acceptance.StationSteps;
import nextstep.utils.AcceptanceTest;
import nextstep.utils.ResponseUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private Long 강남역;
    private Long 역삼역;

    private String email = "a@b.com";
    private String password = "1234";
    private String accessToken;


    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationSteps.지하철역_생성_요청("강남역").jsonPath().getObject("id", Long.class);
        역삼역 = StationSteps.지하철역_생성_요청("역삼역").jsonPath().getObject("id", Long.class);

        LineSteps.지하철_노선_생성_요청("2호선", "green", 강남역, 역삼역, 10);

        MemberSteps.회원_생성_요청(email, password, 20);

        accessToken = AuthStep.로그인(email, password).jsonPath().getString("accessToken");
    }

    /**
     * when: 즐겨 찾기를 생성하면
     * then: 즐겨 찾기가 생성된다.
     */
    @Test
    void 즐겨_찾기_생성() {
        // when
        FavoriteSteps.지하철_좋아요_생성(강남역, 역삼역, accessToken);

        // then
        final ExtractableResponse<Response> favoriteResponse = FavoriteSteps.지하철_좋아요_조회(accessToken);
        assertThat(favoriteResponse).isNotNull();
        ResponseUtils.응답의_STATUS_검증(favoriteResponse, HttpStatus.OK);
    }

    /**
     * given: 경로가 존재하지 않는 두 역이 주어 졌을 때
     * when: 즐겨 찾기를 생성하면
     * then: 즐겨 찾기 생성에 실패한다.
     */
    @Test
    void 즐겨_찾기_생성__비_정상_경로이면_등록이_불가하다() {
        // given
        final Long 사당역 = StationSteps.지하철역_생성_요청("사당역").jsonPath().getObject("id", Long.class);

        // when
        ExtractableResponse<Response> response = FavoriteSteps.지하철_좋아요_생성(강남역, 사당역, accessToken);

        // then
        ResponseUtils.응답의_STATUS_검증(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * given: 즐겨 찾기 생성 시
     * when: 즐겨 찾기를 조회하면
     * then: 즐겨 찾기가 조회된다.
     */
    @Test
    void 즐겨_찾기_조회() {
        // given
        FavoriteSteps.지하철_좋아요_생성(강남역, 역삼역, accessToken);

        // when
        final ExtractableResponse<Response> favoriteResponse = FavoriteSteps.지하철_좋아요_조회(accessToken);

        // then
        assertThat(favoriteResponse).isNotNull();
        ResponseUtils.응답의_STATUS_검증(favoriteResponse, HttpStatus.OK);
    }

    /**
     *  given: 즐겨 찾기가 없을 때
     *  when: 즐겨 찾기를 조회하면
     *  then: 빈 어레이가 리턴된다
     */
    @Test
    void 즐겨_찾기_조회__없으면_빈_어레이가_리턴된다() {
        // when
        final ExtractableResponse<Response> favoriteResponse = FavoriteSteps.지하철_좋아요_조회(accessToken);

        // then
        assertThat(favoriteResponse).isNotNull();
        ResponseUtils.응답의_STATUS_검증(favoriteResponse, HttpStatus.OK);
        assertThat(favoriteResponse.jsonPath().getList(".", FavoriteResponse.class)).isEmpty();
    }
}