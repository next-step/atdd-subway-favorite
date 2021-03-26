package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.FavoriteAlreadyExistException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("지하철 즐겨찾기 비즈니스 로직 단위 테스트")
@SpringBootTest
@Transactional
class FavoriteServiceTest {

    private static final long MEMBER_ID = 1L;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private FavoriteService favoriteService;

    private Station savedStationGangnam;
    private Station savedStationCheonggyesan;
    private FavoriteRequest favoriteRequest;

    @BeforeEach
    void setUp() {
        // given
        savedStationGangnam = stationRepository.save(new Station("강남역"));
        savedStationCheonggyesan = stationRepository.save(new Station("청계산입구역"));

        favoriteRequest = new FavoriteRequest(savedStationGangnam.getId(), savedStationCheonggyesan.getId());
    }

    @Test
    @DisplayName("즐겨찾기 추가")
    void addFavorite() {
        // when
        Long savedFavoriteId = favoriteService.addFavorite(MEMBER_ID, favoriteRequest);

        // then
        assertThat(savedFavoriteId).isNotNull();
    }

    @Test
    @DisplayName("이미 존재하는 즐겨찾기를 추가할 경우 Exception 발생")
    void validateAlreadyFavorite() {
        // given
        favoriteService.addFavorite(MEMBER_ID, favoriteRequest);

        // when & then
        assertThatExceptionOfType(FavoriteAlreadyExistException.class)
                .isThrownBy(() -> favoriteService.addFavorite(MEMBER_ID, favoriteRequest));
    }

    @Test
    @DisplayName("즐겨찾기 조회")
    void findAllFavorite() {
        // given
        favoriteService.addFavorite(MEMBER_ID, favoriteRequest);

        Station savedStationYangJae = stationRepository.save(new Station("양재역"));
        FavoriteRequest favoriteRequest2 = new FavoriteRequest(savedStationGangnam.getId(), savedStationYangJae.getId());
        favoriteService.addFavorite(MEMBER_ID, favoriteRequest2);

        // when
        List<FavoriteResponse> favoriteResponses = favoriteService.findAllFavoriteResponsesByMemberId(MEMBER_ID);

        // then
        assertThat(favoriteResponses).hasSize(2);
    }
}
