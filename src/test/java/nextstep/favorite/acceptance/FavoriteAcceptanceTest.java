package nextstep.favorite.acceptance;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.restassured.RestAssured;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.line.domain.Color;
import nextstep.line.presentation.LineRequest;
import nextstep.line.presentation.SectionRequest;
import nextstep.subway.fixture.LineSteps;
import nextstep.subway.fixture.SectionSteps;
import nextstep.subway.fixture.StationSteps;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    @DisplayName("인증되지 않은 멤버의 요청은 UNAUTHORIZED로 응답온다")
    public void shouldFailIfUnauthorizedMemberRequest() {
        FavoriteSteps.미인증된_유저가_즐겨찾기_생성할수없다();
    }


}