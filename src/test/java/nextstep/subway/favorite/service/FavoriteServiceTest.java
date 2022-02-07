package nextstep.subway.favorite.service;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.repository.FavoriteRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.repository.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FavoriteServiceTest {
    public static final long MEMBER_ID = 1L;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    FavoriteRepository favoriteRepository;

    FavoriteService favoriteService;
    Station 교대역;
    Station 강남역;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, stationRepository);

        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        stationRepository.saveAll(Arrays.asList(교대역, 강남역));
    }

    @Test
    void create() {
        // given
        FavoriteRequest request = FavoriteRequest.of(교대역.getId(), 강남역.getId());

        // when
        Long favoriteId = favoriteService.create(MEMBER_ID, request);

        // then
        assertThat(favoriteId).isNotNull();
    }
}