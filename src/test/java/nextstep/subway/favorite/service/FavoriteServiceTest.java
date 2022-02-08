package nextstep.subway.favorite.service;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.repository.FavoriteRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.repository.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

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
    Station 역삼역;
    Station 선릉역;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, stationRepository);

        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        선릉역 = new Station("선릉역");

        stationRepository.saveAll(Arrays.asList(교대역, 강남역, 역삼역, 선릉역));
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

    @Test
    void findAllByMemberId() {
        // given
        FavoriteRequest request1 = FavoriteRequest.of(교대역.getId(), 강남역.getId());
        favoriteService.create(MEMBER_ID, request1);
        FavoriteRequest request2 = FavoriteRequest.of(강남역.getId(), 선릉역.getId());
        favoriteService.create(MEMBER_ID, request2);

        // when
        List<FavoriteResponse> favorites = favoriteService.findAllByMemberId(MEMBER_ID);

        // then
        assertThat(favorites).hasSize(2);
    }

    @Test
    void delete() {
        // given
        FavoriteRequest request = FavoriteRequest.of(교대역.getId(), 강남역.getId());
        Long favoriteId = favoriteService.create(MEMBER_ID, request);

        // when
        favoriteService.deleteFavorite(MEMBER_ID, favoriteId);
        List<FavoriteResponse> favorites = favoriteService.findAllByMemberId(MEMBER_ID);

        // then
        assertThat(favorites).hasSize(0);
    }
}