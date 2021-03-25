package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private StationService stationService;

    private StationResponse source;
    private StationResponse target;
    FavoriteResponse favoriteResponse;

    private final Long MEMBER_ID = 1L;

    @BeforeEach
    void setup() {
        source = stationService.saveStation(new StationRequest("source"));
        target = stationService.saveStation(new StationRequest("target"));
        FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());
        favoriteResponse = favoriteService.addFavorite(MEMBER_ID, favoriteRequest);
    }

    @DisplayName("즐겨찾기 추가")
    @Test
    void addFavorite() {
        // then
        assertThat(favoriteResponse).isNotNull();
        assertThat(favoriteResponse.getTarget()).isEqualTo(target);
        assertThat(favoriteResponse.getSource()).isEqualTo(source);
    }

    @DisplayName("즐겨찾기 중복생성 금지")
    @Test
    void duplicateFavorite() {

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> favoriteService
                        .addFavorite(MEMBER_ID, new FavoriteRequest(source.getId(), target.getId())));
    }

    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void findAllFavorite() {
        // given
        StationResponse secondTarget = stationService.saveStation(new StationRequest("second"));
        FavoriteResponse secondFavorite = favoriteService.addFavorite(MEMBER_ID,
                new FavoriteRequest(source.getId(), secondTarget.getId()));

        // when
        List<FavoriteResponse> favorites = favoriteService.findAllFavorites(MEMBER_ID);

        // then
        assertThat(favorites)
                .containsAll(Arrays.asList(favoriteResponse, secondFavorite));
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        // when
        favoriteService.removeFavorite(favoriteResponse.getId());
        List<FavoriteResponse> favorites = favoriteService.findAllFavorites(MEMBER_ID);

        // then
        assertThat(favorites).isEmpty();
    }
}
