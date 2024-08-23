package nextstep.favorite.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.application.exception.NotExistFavoriteException;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.path.application.PathService;
import nextstep.station.application.StationService;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static nextstep.utils.UnitTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("즐겨찾기 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationService stationService;
    @Mock
    private PathService pathService;

    private Member member;
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "admin@email.com", "password", 20);
        favoriteService = new FavoriteService(favoriteRepository, stationService, pathService);
    }

    @DisplayName("즐겨찾기 추가 함수는, 시작역과 종료역을 입력받아 즐겨찾기에 추가하고 추가된 즐겨찾기 정보를 반환한다.")
    @Test
    void createFavoriteTest() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());
        Favorite favorite = new Favorite(강남역.getId(), 양재역.getId(), member.getId());
        mockDependencies(favorite);

        // when
        FavoriteResponse actual = favoriteService.createFavorite(member.getId(), favoriteRequest);

        // then
        assertThat(actual).isEqualTo(FavoriteResponse.of(favorite.getId(), 강남역, 양재역));
    }

    @DisplayName("즐겨찾기 추가 함수는, 정상적인 경로인지 확인하는 함수를 실행한다.")
    @Test
    void createFavoriteNotConnectedStationsTest() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());
        Favorite favorite = new Favorite(강남역.getId(), 양재역.getId(), member.getId());
        mockDependencies(favorite);

        // when
        favoriteService.createFavorite(member.getId(), favoriteRequest);

        // then
        verify(pathService, times(1)).validatePaths(강남역.getId(), 양재역.getId());
    }

    private void mockDependencies(Favorite favorite) {
        when(stationService.lookUp(강남역.getId())).thenReturn(강남역);
        when(stationService.lookUp(양재역.getId())).thenReturn(양재역);
        when(favoriteRepository.save(favorite)).thenReturn(favorite);
        doNothing().when(pathService).validatePaths(any(), any());
    }

    @DisplayName("즐겨찾기 조회 함수는, 주어진 회원의 즐겨찾기 목로을 반환한다.")
    @Test
    void findFavoritesTest() {
        // given
        Favorite favorite1 = new Favorite(1L, 강남역.getId(), 양재역.getId(), member.getId());
        Favorite favorite2 = new Favorite(2L, 교대역.getId(), 홍대역.getId(), member.getId());
        List<Favorite> favorites = List.of(favorite1, favorite2);

        when(stationService.lookUp(강남역.getId())).thenReturn(강남역);
        when(stationService.lookUp(양재역.getId())).thenReturn(양재역);
        when(stationService.lookUp(교대역.getId())).thenReturn(교대역);
        when(stationService.lookUp(홍대역.getId())).thenReturn(홍대역);
        when(favoriteRepository.findByMemberId(member.getId())).thenReturn(favorites);

        // when
        List<FavoriteResponse> actual = favoriteService.findFavorites(member.getId());

        // then
        assertThat(actual).isEqualTo(예상_즐겨찾기_목록());
    }

    private static List<FavoriteResponse> 예상_즐겨찾기_목록() {
        return List.of(FavoriteResponse.of(1L, 강남역, 양재역),
                FavoriteResponse.of(2L, 교대역, 홍대역));
    }

    @DisplayName("즐겨찾기 삭제 함수는, favoriteRepository의 즐겨찾기 삭제 함수가 실행된다.")
    @Test
    void deleteFavoritesTest() {
        // given
        final Long favoriteId = 1L;

        when(favoriteRepository.findByIdAndMemberId(favoriteId, member.getId()))
                .thenReturn(Optional.of(new Favorite(favoriteId, 강남역.getId(), 양재역.getId(), member.getId())));

        // when
        favoriteService.deleteFavorite(favoriteId, member.getId());

        // then
        verify(favoriteRepository).deleteById(favoriteId);
    }

    @DisplayName("즐겨찾기 삭제 함수는, 존재하지 않는 즐겨찾기를 삭제하려 하면 NotExistFavoriteException이 발생한다.")
    @Test
    void deleteFavoriteNotExistFavoriteExceptionTest() {
        // given
        when(favoriteRepository.findByIdAndMemberId(any(), any())).thenReturn(Optional.empty());

        // when
        ThrowingCallable actual = () -> favoriteService.deleteFavorite(1L, member.getId());

        // then
        assertThatThrownBy(actual).isInstanceOf(NotExistFavoriteException.class);
    }
}
