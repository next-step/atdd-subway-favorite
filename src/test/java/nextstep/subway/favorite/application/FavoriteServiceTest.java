package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("지하철 즐겨찾기 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    private static final long SOURCE = 1L;
    private static final long TARGET = 2L;
    public static final long MEMBER_ID = 1L;
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;
    public static final long ID = 1L;

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationRepository stationRepository;

    private LoginMember loginMember;
    private FavoriteService favoriteService;
    private Favorite expected;
    private FavoriteRequest request;

    private List<Station> stations;

    private Station station1;
    private Station station2;
    private Station station3;

    private Long station1Id;
    private Long station2Id;
    private Long station3Id;

    private Set<Long> stationIds;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, stationRepository);
        expected = new Favorite(ID, MEMBER_ID, SOURCE, TARGET);
        request = new FavoriteRequest(SOURCE, TARGET);
        loginMember = new LoginMember(ID, EMAIL, PASSWORD, AGE);

        station1 = new Station("강남역");
        ReflectionTestUtils.setField(station1, "id", 1L);
        station1Id = station1.getId();

        station2 = new Station("역삼역");
        ReflectionTestUtils.setField(station2, "id", 2L);
        station2Id = station2.getId();

        station3 = new Station("양재역");
        ReflectionTestUtils.setField(station3, "id", 3L);
        station3Id = station3.getId();

        stations = Lists.newArrayList(station1, station2, station3);
        stationIds = stations.stream().map(Station::getId).collect(Collectors.toSet());
    }

    @Test
    void create() {
        // given
        when(favoriteRepository.save(any())).thenReturn(expected);

        // when
        Favorite actual = favoriteService.createFavorite(MEMBER_ID, request);

        // then
        assertThat(actual).isSameAs(expected);
    }

    @Test
    void delete() {
        // given
        when(favoriteRepository.findByIdAndMemberId(ID, MEMBER_ID))
                .thenReturn(Optional.ofNullable(expected));

        // when
        favoriteService.deleteFavorite(MEMBER_ID, SOURCE);

        // then
        verify(favoriteRepository).findByIdAndMemberId(SOURCE, MEMBER_ID);
    }

    @Test
    void deleteWhenNotFound() {
        // given
        when(favoriteRepository.findByIdAndMemberId(ID, MEMBER_ID))
                .thenThrow(FavoriteNotFoundException.class);

        // when
        assertThatExceptionOfType(FavoriteNotFoundException.class)
                .isThrownBy(() -> favoriteService.deleteFavorite(MEMBER_ID, SOURCE));
    }

    @Test
    void findFavorite() {
        // given
        Favorite favorite = new Favorite(1L, MEMBER_ID, station1Id, station2Id);
        Favorite favorite2 = new Favorite(2L, MEMBER_ID, station1Id, station3Id);
        Favorite favorite3 = new Favorite(3L, MEMBER_ID, station2Id, station3Id);
        List<Favorite> favorites = Arrays.asList(favorite, favorite2, favorite3);

        when(favoriteRepository.findAllByMemberId(MEMBER_ID)).thenReturn(favorites);
        when(stationRepository.findAllById(stationIds)).thenReturn(stations);

        // when
        List<FavoriteResponse> responses = favoriteService.findFavorites(MEMBER_ID);

        // then
        assertAll(
                () -> assertThat(responses).hasSize(3),
                () -> assertThat(responses.get(0).getId()).isEqualTo(station1Id),
                () -> assertThat(responses.get(1).getId()).isEqualTo(station2Id),
                () -> assertThat(responses.get(2).getId()).isEqualTo(station3Id)
        );
    }

    @Test
    void findFavoriteWhenNotFound() {
        // given
        when(favoriteRepository.findAllByMemberId(MEMBER_ID)).thenReturn(Lists.emptyList());

        // when
        List<FavoriteResponse> favorites = favoriteService.findFavorites(MEMBER_ID);

        // then
        assertThat(favorites).isEmpty();
    }

}
