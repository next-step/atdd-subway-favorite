package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private FavoriteService favoriteService;

    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 삼성역;

    @BeforeEach
    void setUp(){
        강남역 = StationResponse.of(stationRepository.save(new Station("강남역")));
        역삼역 = StationResponse.of(stationRepository.save(new Station("역삼역")));
        삼성역 = StationResponse.of(stationRepository.save(new Station("삼성역")));
    }

    @Test
    void addFavorite() {
        FavoriteResponse favorite = favoriteService.createFavorite(1L,new FavoriteRequest(강남역.getId(),역삼역.getId()));
        assertThat(favorite.getId()).isNotNull();
    }

    @Test
    void deleteFavorites() {
        favoriteService.createFavorite(1L,new FavoriteRequest(강남역.getId(),역삼역.getId()));
        favoriteService.createFavorite(1L,new FavoriteRequest(역삼역.getId(),삼성역.getId()));

        favoriteService.deleteFavorite(1L,2L);

        assertThat(favoriteService.getFavorites(1L).size()).isEqualTo(1);
    }

    @Test
    void getFavorites() {
        FavoriteResponse favorite = favoriteService.createFavorite(1L,new FavoriteRequest(강남역.getId(),역삼역.getId()));

        List<FavoriteResponse> favorites = favoriteService.getFavorites(1L);
        assertThat(favorites.size()).isEqualTo(1);
    }

}
