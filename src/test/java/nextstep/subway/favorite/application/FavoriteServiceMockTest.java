package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.IsExistFavoriteException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceMockTest {

    private final long MEMBER_ID = 1L;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationService stationService;

    @Autowired
    private FavoriteService favoriteService;

    private Station source;
    private Station target;
    private Favorite favorite;
    private FavoriteRequest favoriteRequest;

    @BeforeEach
    void setup() {
        favoriteService = new FavoriteService(favoriteRepository, stationService);

        source = new Station("source");
        ReflectionTestUtils.setField(source, "id", 1L);
        when(stationService.findById(source.getId())).thenReturn(source);

        target = new Station("target");
        ReflectionTestUtils.setField(target, "id", 2L);
        when(stationService.findById(target.getId())).thenReturn(target);

        favorite = new Favorite(MEMBER_ID, source, target);
        ReflectionTestUtils.setField(favorite, "id", 1L);


        favoriteRequest = new FavoriteRequest(
            source.getId(),
            target.getId()
        );

        when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);
    }

    @DisplayName("즐겨찾기를 추가한다.")
    @Test
    public void 즐겨찾기_추가_테스트() {
        // when
        FavoriteResponse favoriteResponse = favoriteService.addFavorite(
            MEMBER_ID,
            favoriteRequest
        );


        // then
        assertThat(favoriteResponse).isNotNull();
        assertThat(favoriteResponse.getTarget()).isEqualTo(StationResponse.of(target));
        assertThat(favoriteResponse.getSource()).isEqualTo(StationResponse.of(source));
    }

    @DisplayName("즐겨찾기를 중복으로 추가할 경우 Exception이 발생한.")
    @Test
    public void 즐겨찾기_중복_추가_테스트() {
        // given
        favoriteService.addFavorite(MEMBER_ID, favoriteRequest);

        // when - then
        when(favoriteRepository.existsByMemberIdAndSourceAndTarget(MEMBER_ID, source, target)).thenReturn(true);
        assertThatExceptionOfType(IsExistFavoriteException.class)
            .isThrownBy(() -> favoriteService.addFavorite(MEMBER_ID, favoriteRequest));
    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    public void 즐겨찾기_목록_조회_테스트() {
        // given
        when(favoriteRepository.findAllByMemberId(MEMBER_ID)).thenReturn(Collections.singletonList(favorite));
        FavoriteResponse response = favoriteService.addFavorite(MEMBER_ID, favoriteRequest);

        // when
        List<FavoriteResponse> favorites = favoriteService.findAllOfMember(MEMBER_ID);

        // then
        assertThat(favorites)
            .containsAll(Collections.singletonList(FavoriteResponse.of(favorite)));
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    public void 즐겨찾기_삭제_테스트() {
        when(favoriteRepository.findAllByMemberId(MEMBER_ID)).thenReturn(Collections.emptyList());

        // given
        FavoriteResponse favoriteResponse = favoriteService.addFavorite(MEMBER_ID, favoriteRequest);

        // when
        favoriteService.removeFavorite(favoriteResponse.getId());
        List<FavoriteResponse> favorites = favoriteService.findAllOfMember(MEMBER_ID);

        // then
        assertThat(favorites).isEmpty();

    }
}
