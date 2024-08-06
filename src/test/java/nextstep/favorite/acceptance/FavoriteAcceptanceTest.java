package nextstep.favorite.acceptance;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.acceptance.AuthAssuredTemplate;
import nextstep.member.acceptance.MemberSteps;
import nextstep.subway.common.SubwayErrorMessage;
import nextstep.subway.line.LineAssuredTemplate;
import nextstep.subway.line.SectionAssuredTemplate;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.StationAssuredTemplate;
import nextstep.subway.station.StationFixtures;
import nextstep.subway.station.StationResponse;
import nextstep.utils.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.favorite.acceptance.FavoriteAssuredTemplate.*;
import static nextstep.subway.line.LineAssuredTemplate.*;
import static nextstep.subway.line.SectionAssuredTemplate.*;
import static nextstep.subway.station.StationAssuredTemplate.*;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private long 논현역_id;
    private long 양재역_id;
    private long 고속터미널역_id;
    private long 교대역_id;
    private long 사당역_id;
    private long 강남역_id;

    private String email = "email";
    private String password = "password";
    private int age = 20;

    private String accessToken;

    @BeforeEach
    void setUp() {
        this.강남역_id = createStationWithId(StationFixtures.강남역.getName());
        this.양재역_id = createStationWithId(StationFixtures.양재역.getName());
        this.논현역_id = createStationWithId(StationFixtures.논현역.getName());
        this.고속터미널역_id = createStationWithId(StationFixtures.고속터미널역.getName());
        this.교대역_id = createStationWithId(StationFixtures.교대역.getName());

        long 신분당선_id = createLine(new LineRequest("신분당선", "green", 논현역_id, 강남역_id, 4L)).then().extract().jsonPath().getLong("id");
        addSection(신분당선_id, new SectionRequest(강남역_id, 양재역_id, 3L));

        long 삼호선_id = createLine(new LineRequest("3호선", "orange", 논현역_id, 고속터미널역_id, 2L)).then().extract().jsonPath().getLong("id");
        addSection(삼호선_id, new SectionRequest(고속터미널역_id, 교대역_id, 1L));
        addSection(삼호선_id, new SectionRequest(교대역_id, 양재역_id, 3L));

        this.사당역_id = createStationWithId(StationFixtures.사당역.getName());

        MemberSteps.회원_생성_요청(email, password, age);
        this.accessToken = AuthAssuredTemplate.로그인(email, password)
                .then().extract().jsonPath().getString("accessToken");
    }

    /**
     * given 회원가입 후 로그인을 통해 토큰을 전달받습니다.
     * when 전달받은 토큰으로 즐겨찾기 요청을 진행합니다, 서로 연결되어 있는 지하철 역이라면 정상 응답을 반환합니다.
     * then 이후 해당 즐겨찾기를 요청하면 등록한 역 목록이 보입니다.
     */
    @DisplayName("서로 연결되어 있는 경우 정상적으로 즐겨찾기가 가능합니다.")
    @Test
    void successFavorite() {
        // given
        즐겨찾기_등록(accessToken, 논현역_id, 양재역_id);
        즐겨찾기_등록(accessToken, 강남역_id, 양재역_id);

        // when
        ExtractableResponse<Response> result = 즐겨찾기_조회(accessToken)
                .then().log().all()
                .extract();

        // then
        Assertions.assertThat(result.body().as(new TypeRef<List<FavoriteResponse>>() {})).hasSize(2)
                .extracting("source", "target")
                .containsExactly(
                        Tuple.tuple(new StationResponse(논현역_id, StationFixtures.논현역.getName()), new StationResponse(양재역_id, StationFixtures.양재역.getName())),
                        Tuple.tuple(new StationResponse(강남역_id, StationFixtures.강남역.getName()), new StationResponse(양재역_id, StationFixtures.양재역.getName()))
                );
    }

    /**
     * given 회원가입 후 로그인을 통해 토큰을 발급받습니다. 이후 즐겨찾기를 2개 등록합니다.
     * when 2번째 즐겨찾기를 삭제합니다.
     * then 즐겨찾기를 조회하면 1개의 즐겨찾기만 조회됩니다.
     */
    @DisplayName("즐겨찾기를 삭제한 후 조회하면 해당 즐겨찾기는 목록에 존재하지 않습니다.")
    @Test
    void deleteFavorite() {
        // given
        즐겨찾기_등록(accessToken, 논현역_id, 양재역_id);
        String location = 즐겨찾기_등록(accessToken, 강남역_id, 양재역_id)
                .then().extract().header(HttpHeaders.LOCATION);

        String[] split = location.split("/");
        String favoriteId = split[split.length - 1];

        // when
        즐겨찾기_석제(accessToken, Long.valueOf(favoriteId));

        // then
        ExtractableResponse<Response> result = 즐겨찾기_조회(accessToken)
                .then().log().all().extract();

        Assertions.assertThat(result.body().as(new TypeRef<List<FavoriteResponse>>() {})).hasSize(1)
                .extracting("source", "target")
                .containsExactly(
                        Tuple.tuple(new StationResponse(논현역_id, StationFixtures.논현역.getName()), new StationResponse(양재역_id, StationFixtures.양재역.getName()))
                );
    }

    /**
     * given 회원가입 후 로그인을 통해 토큰을 전달받습니다.
     * when 전달받은 토큰으로 즐겨찾기 요청을 진행합니다.
     * then 서로 연결되어 있지 않은 역을 요청했기 때문에 에러응답을 전달받습니다.
     */
    @DisplayName("즐겨찾기를 생성할 때 source 와 target 이 연결되어 있지 않다면 에러응답을 전달받습니다.")
    @Test
    void notConnect() {
        // given
        // when
        ExtractableResponse<Response> result = 즐겨찾기_등록(accessToken, 사당역_id, 양재역_id)
                .then().extract();

        // then
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(result.body().asString()).isEqualTo(SubwayErrorMessage.NOT_CONNECTED_STATION.getMessage());
    }

    /**
     * given 회원가입 후 로그인을 통해 토큰을 전달받습니다.
     * when 전달받은 토큰으로 즐겨찾기 요청을 진행합니다.
     * then 서로 동일한 source, target 역을 요청했기 때문에 에러응답을 전달받습니다.
     */
    @DisplayName("즐겨찾기를 생성할 때 source 와 target 이 같다면 에러 응답을 전달받습니다.")
    @Test
    void sameStation() {
        // given
        // when
        ExtractableResponse<Response> result = 즐겨찾기_등록(accessToken, 사당역_id, 사당역_id)
                .then().extract();
        // then
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(result.body().asString()).isEqualTo(SubwayErrorMessage.NOT_CONNECTED_STATION.getMessage());
    }

    /**
     * given 회원가입 후 로그인을 통해 토큰을 전달받습니다.
     * when 전달받은 토큰으로 즐겨찾기 요청을 진행합니다, 서로 연결되어 있는 지하철 역이라면 정상 응답을 반환합니다.
     * then 등록하지 않은 즐겨찾기를 삭제하는 경우 에러 응답을 받습니다.
     */
    @DisplayName("등록한 즐겨찾기가 아닌 데이터를 삭제하는 경우 에러 응답을 받습니다.")
    @Test
    void notEnrollFavorite() {
        // given
        즐겨찾기_등록(accessToken, 논현역_id, 양재역_id);
        즐겨찾기_등록(accessToken, 강남역_id, 양재역_id);

        // when
        ExtractableResponse<Response> result = 즐겨찾기_석제(accessToken, (long) Integer.MAX_VALUE)
                .then().extract();

        // then
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest(name = "잘못된 토큰으로 삭제요청을 보내는 경우 에러 응답을 전달받습니다.")
    @ValueSource(strings = {"", "Bearer   asd", "   bearer asdf"})
    void noToken(String accessToken) {
        // given
        즐겨찾기_등록(this.accessToken, 논현역_id, 양재역_id);
        즐겨찾기_등록(this.accessToken, 강남역_id, 양재역_id);
        // when
        ExtractableResponse<Response> result = 즐겨찾기_석제(accessToken, (long) Integer.MAX_VALUE)
                .then().extract();
        // then
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}



















