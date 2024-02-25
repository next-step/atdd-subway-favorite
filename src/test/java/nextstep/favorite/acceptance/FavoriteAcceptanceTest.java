package nextstep.favorite.acceptance;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.restassured.RestAssured;
import nextstep.favorite.application.dto.FavoriteResponse;
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


}