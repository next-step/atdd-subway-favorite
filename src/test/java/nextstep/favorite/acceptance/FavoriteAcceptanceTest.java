package nextstep.favorite.acceptance;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.line.domain.Color;
import nextstep.line.presentation.LineRequest;
import nextstep.line.presentation.SectionRequest;
import nextstep.member.acceptance.MemberSteps;
import nextstep.subway.fixture.LineSteps;
import nextstep.subway.fixture.SectionSteps;
import nextstep.subway.fixture.StationSteps;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
@AcceptanceTest
@Transactional
public class FavoriteAcceptanceTest {

    @LocalServerPort
    private int port;

    long 강변역;
    long 구의역;
    long 건대입구역;
    long 잠실역;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        강변역 = StationSteps.createStation("강변역").getId();
        구의역 = StationSteps.createStation("구의역").getId();
        건대입구역 = StationSteps.createStation("건대입구역").getId();
        잠실역 = StationSteps.createStation("잠실역").getId();
        long 이호선 = LineSteps.노선_생성(new LineRequest("이호선", Color.GREEN, 강변역, 구의역, 19)).getId();
        SectionSteps.라인에_구간을_추가한다(이호선, new SectionRequest(구의역, 건대입구역, 4));
        SectionSteps.라인에_구간을_추가한다(이호선, new SectionRequest(건대입구역, 잠실역, 4));
    }

    @Test
    @DisplayName("즐겨찾기 생성한다")
    public void shouldCreateFavorite() {

        // given
        long 즐겨찾기 = FavoriteSteps.즐겨찾기_생성한다(강변역, 건대입구역);

        // when
        FavoriteResponse favoriteResponse = FavoriteSteps.즐겨찾기_조회한다(즐겨찾기);

        // then
        assertThat(favoriteResponse.getSource().getId()).isEqualTo(강변역);
        assertThat(favoriteResponse.getTarget().getId()).isEqualTo(건대입구역);
    }

    @Test
    @DisplayName("나의 즐겨찾기 조회한다")
    public void shouldFindMyFavorite() {

        long 즐겨찾기1 = FavoriteSteps.즐겨찾기_생성한다(강변역, 건대입구역);
        long 즐겨찾기2 = FavoriteSteps.즐겨찾기_생성한다(강변역, 잠실역);

        // when
        List<FavoriteResponse> 모든_즐겨찾기 = FavoriteSteps.모든_즐겨찾기_조회한다();

        // then
        List<Long> ids = 모든_즐겨찾기.stream().map(FavoriteResponse::getId).collect(Collectors.toList());
        assertThat(ids).containsExactly(즐겨찾기1, 즐겨찾기2);
    }


    @Test
    @DisplayName("나의 즐겨찾기 삭제한다")
    public void shouldDeleteMyFavorite() {
        // given
        long 즐겨찾기 = FavoriteSteps.즐겨찾기_생성한다(강변역, 건대입구역);

        // when
        FavoriteSteps.즐겨찾기_삭제한다(즐겨찾기);

        // then
        Assertions.assertThrows(UnrecognizedPropertyException.class,
                () -> FavoriteSteps.즐겨찾기_조회한다(즐겨찾기));

    }

    @Test
    @DisplayName("노선에 등록되지 않은 역으로 즐겨찾기할 수 없다")
    public void shouldFailIfFavoriteStationsAreNotExist() {

        // given
        long UNKNOWN_RATION = Long.MAX_VALUE;

        // when
        Assertions.assertThrows(
                AssertionError.class,
                () -> FavoriteSteps.즐겨찾기_생성한다(강변역, UNKNOWN_RATION));
    }

    @Test
    @DisplayName("인증되지 않은 멤버의 요청은 UNAUTHORIZED로 응답온다")
    public void shouldFailIfUnauthorizedMemberRequest() {
        FavoriteSteps.미인증된_유저가_즐겨찾기_생성할수없다();
    }

    
    @Test
    @DisplayName("다른 유저의 즐겨찾기를 삭제할 수 없다")
    public void shouldFail_IfTryOtherMemberFavorite() {

        var TOKEN = 다른_유저의_토큰_생성하기();
        var requestSpecification = new RequestSpecBuilder()
                .addHeader("Authorization", TOKEN)
                .build();

        Map<String, Long> param2 = new HashMap<>();
        param2.put("source", 강변역);
        param2.put("target", 건대입구역);

        // given
        long 다른_유저의_즐겨찾기 = FavoriteSteps.즐겨찾기_생성한다(강변역, 건대입구역);

        RestAssured
                .given()
                .spec(requestSpecification)
                .log().all()
                .body(param2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/favorites/" + 다른_유저의_즐겨찾기)
                .then().log().all()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .extract()
                .response();
    }

    private static String 다른_유저의_토큰_생성하기() {
        String EMAIL = "otherUser@naver.com";
        String PASSWORD = "password";
        int AGE = 12;

        MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);
        Map<String, Object> param = new HashMap<>();
        param.put("email", EMAIL);
        param.put("password", PASSWORD);
        param.put("age", AGE);
        var TOKEN = "bearer " + MemberSteps.토큰_생성(param);
        return TOKEN;
    }


}