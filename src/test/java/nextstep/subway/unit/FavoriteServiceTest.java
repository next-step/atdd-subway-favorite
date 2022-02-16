package nextstep.subway.unit;

import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관리 - 실객체")
@SpringBootTest
@Transactional
class FavoriteServiceTest {
    private static final Long 사용자 = 1L;

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private FavoriteService favoriteService;

    @DisplayName("즐겨찾기를 생성한다")
    @Test
    void craeteFavorite() {
        // given
        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 역삼역 = stationRepository.save(new Station("역삼역"));

        // when
        FavoriteResponse favorite = favoriteService.createFavorite(사용자, createRequest(강남역, 역삼역));

        // then
        assertThat(favorite).isNotNull();
    }

    @DisplayName("즐겨찾기를 조회")
    @Test
    void getFavorites() {
        // given
        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 역삼역 = stationRepository.save(new Station("역삼역"));

        favoriteService.createFavorite(사용자, createRequest(강남역, 역삼역));

        // when
        List<FavoriteResponse> list = favoriteService.findAll(사용자);

        // then
        assertThat(list).hasSize(1);
    }

    @DisplayName("즐겨찾기를 제거")
    @Test
    void deleteFavorite() {
        // given
        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 역삼역 = stationRepository.save(new Station("역삼역"));

        FavoriteResponse favorite = favoriteService.createFavorite(사용자, createRequest(강남역, 역삼역));

        // when
        favoriteService.removeFavorite(사용자, favorite.getId());

        // then
        List<FavoriteResponse> list = favoriteService.findAll(사용자);
        assertThat(list).hasSize(0);
    }

    private FavoriteRequest createRequest(Station source, Station target) {
        return new FavoriteRequest(source.getId(), target.getId());
    }
}
