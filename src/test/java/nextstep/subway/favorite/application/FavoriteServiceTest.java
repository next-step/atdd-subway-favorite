package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.FavoriteAlreadyExistException;
import nextstep.subway.favorite.exception.InvalidFavoriteMemberException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<Long> resultStationResponsesIds = getResultStationResponsesIds(favoriteResponses);
        assertThat(resultStationResponsesIds).containsAll(Arrays.asList(savedStationGangnam.getId(), savedStationYangJae.getId(), savedStationCheonggyesan.getId()));
    }

    @Test
    @DisplayName("즐겨찾기 제거")
    void removeFavorite() {
        // given
        Long addedFavoriteId1 = favoriteService.addFavorite(MEMBER_ID, favoriteRequest);

        Station savedStationYangJae = stationRepository.save(new Station("양재역"));
        FavoriteRequest favoriteRequest2 = new FavoriteRequest(savedStationGangnam.getId(), savedStationYangJae.getId());
        favoriteService.addFavorite(MEMBER_ID, favoriteRequest2);

        // when
        favoriteService.removeFavorite(addedFavoriteId1, MEMBER_ID);

        // then
        List<FavoriteResponse> favoriteResponses = favoriteService.findAllFavoriteResponsesByMemberId(MEMBER_ID);
        assertThat(favoriteResponses).hasSize(1);
    }

    @Test
    @DisplayName("자신이 등록하지 않은 즐겨찾기 제거시 Exception 발생")
    void validateRemoveOtherFavorite() {
        // given
        Long addedFavoriteId = favoriteService.addFavorite(MEMBER_ID, favoriteRequest);

        // when & then
        assertThatExceptionOfType(InvalidFavoriteMemberException.class)
                .isThrownBy(() -> favoriteService.removeFavorite(addedFavoriteId, 2L));
    }

    private List<Long> getResultStationResponsesIds(List<FavoriteResponse> favoriteResponses) {
        return favoriteResponses.stream()
                .flatMap(favoriteResponse -> Stream.of(favoriteResponse.getSource(), favoriteResponse.getTarget()))
                .map(StationResponse::getId)
                .distinct()
                .collect(Collectors.toList());
    }
}
