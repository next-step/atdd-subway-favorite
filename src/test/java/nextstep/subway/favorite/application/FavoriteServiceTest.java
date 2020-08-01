package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@DisplayName("지하철 즐겨찾기 서비스 단위 테스트")
@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    private static final Long SOURCE_STATION_ID = 1L;
    private static final Long TARGET_STATION_ID = 2L;
    private static final Long LOGINED_MEMBER_ID = 1L;
    private static final Long FAVORITE_ID = 1L;
    private static final String EMAIL = "javajigi@email.com";
    private static final String PASSWORD = "pobiconan";
    private static final Integer AGE = 20;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationRepository stationRepository;

    private FavoriteService favoriteService;
    private Favorite expectedFavorite;
    private FavoriteRequest favoriteRequest;

    private LoginMember loginMember;
    private Station stationFirst;
    private Station stationSecond;
    private Station stationThird;
    private List<Station> stations;
    private Set<Long> stationIds;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, stationRepository);
        expectedFavorite = new Favorite(FAVORITE_ID, LOGINED_MEMBER_ID, SOURCE_STATION_ID, TARGET_STATION_ID);
        favoriteRequest = new FavoriteRequest(SOURCE_STATION_ID, TARGET_STATION_ID);

        loginMember = new LoginMember(LOGINED_MEMBER_ID, EMAIL, PASSWORD, AGE);

        stationFirst = new Station("강남역");
        ReflectionTestUtils.setField(stationFirst, "id", 1L);
        setLocalDateTime(stationFirst);

        stationSecond = new Station("역삼역");
        ReflectionTestUtils.setField(stationSecond, "id", 2L);
        setLocalDateTime(stationSecond);

        stationThird = new Station("양재역");
        ReflectionTestUtils.setField(stationThird, "id", 3L);
        setLocalDateTime(stationThird);
        stations = Arrays.asList(stationFirst, stationSecond, stationThird);

        stationIds = stations.stream()
            .map(Station::getId)
            .collect(Collectors.toSet());
    }

    @DisplayName("하나의 멤버에 새로운 즐겨찾기를 등록한다.")
    @Test
    void 즐겨찾기를_등록한다() {
        // given: Repository의 반환값을 명시한다.
        when(favoriteRepository.save(any())).thenReturn(expectedFavorite);

        // when: 즐겨찾기를 반환한다.
        Favorite actualFavorite = favoriteService.createFavorite(LOGINED_MEMBER_ID, favoriteRequest);

        // then: 즐겨찾기의 기댓값과 비교한다.
        assertThat(actualFavorite).isEqualTo(expectedFavorite);
    }

    @DisplayName("하나의 멤버에 존재하는 즐겨찾기를 삭제한다.")
    @Test
    void 즐겨찾기를_삭제한다() {
        // given: Repository에서 해당하는 즐겨찾기와 이에 해당하는 로그인 멤버를 꺼낸다고 가정한다.
        when(favoriteRepository.findByMemberIdAndId(LOGINED_MEMBER_ID, FAVORITE_ID))
            .thenReturn(Optional.ofNullable(expectedFavorite));

        // when: 가져온 즐겨찾기를 삭제한다.
        favoriteService.deleteFavorite(LOGINED_MEMBER_ID, FAVORITE_ID);

        // then: 삭제 메소드가 실행되었는 지 확인한다.
        verify(favoriteRepository).findByMemberIdAndId(LOGINED_MEMBER_ID, FAVORITE_ID);
        verify(favoriteRepository).deleteById(FAVORITE_ID);
    }

    @DisplayName("하나의 멤버에 존재하는 즐겨찾기를 삭제하려고 할 때, 존재하지 않는 즐겨찾기에 접근하면 에러를 반환한다.")
    @Test
    void 존재하지_않는_즐겨찾기를_삭제하려면_오류를_반환한다() {
        // given
        when(favoriteRepository.findByMemberIdAndId(LOGINED_MEMBER_ID, FAVORITE_ID))
            .thenThrow(FavoriteNotFoundException.class);

        // when
        assertThatThrownBy(
            () -> favoriteService.deleteFavorite(LOGINED_MEMBER_ID, FAVORITE_ID)
        ).isInstanceOf(FavoriteNotFoundException.class);
    }

    @DisplayName("자신의 즐겨찾기 목록을 반환받을 수 있다.")
    @Test
    void 자신의_즐겨찾기_목록을_반환한다() {
        // given
        Favorite favoriteFirst = new Favorite(1L, LOGINED_MEMBER_ID, stationFirst.getId(), stationSecond.getId());
        Favorite favoriteSecond = new Favorite(2L, LOGINED_MEMBER_ID, stationFirst.getId(), stationThird.getId());
        Favorite favoriteThird = new Favorite(3L, LOGINED_MEMBER_ID, stationSecond.getId(), stationThird.getId());
        List<Favorite> favorites = Arrays.asList(favoriteFirst, favoriteSecond, favoriteThird);
        when(favoriteRepository.findAllByMemberId(LOGINED_MEMBER_ID)).thenReturn(Optional.of(favorites));
        when(stationRepository.findAllById(stationIds)).thenReturn(stations);

        // when
        List<FavoriteResponse> responses = favoriteService.findFavorites(LOGINED_MEMBER_ID);

        // then
        assertAll(
            () -> assertThat(responses).hasSize(3),
            () -> assertThat(responses.get(0).getId()).isEqualTo(favoriteFirst.getId()),
            () -> assertThat(responses.get(1).getId()).isEqualTo(favoriteSecond.getId()),
            () -> assertThat(responses.get(2).getId()).isEqualTo(favoriteThird.getId())
        );
    }

    private void setLocalDateTime(Station station) {
        ReflectionTestUtils.setField(station, "createdDate", LocalDateTime.now());
        ReflectionTestUtils.setField(station, "modifiedDate", LocalDateTime.now());
    }
}
