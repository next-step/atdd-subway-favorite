package nextstep.favorite.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.application.exception.NotExistFavoriteException;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.line.domain.Section;
import nextstep.line.domain.SectionRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.path.application.exception.NotAddedStationsToPathsException;
import nextstep.path.application.exception.NotConnectedPathsException;
import nextstep.station.application.StationService;
import nextstep.station.domain.Station;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static nextstep.utils.UnitTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("즐겨찾기 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private StationService stationService;
    @Mock
    private MemberService memberService;

    private List<Section> 연결되지_않은_두_구간;
    private Member member;
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        연결되지_않은_두_구간 = List.of(강남역_양재역, 교대역_홍대역);
        member = new Member(1L, "admin@email.com", "password", 20);
        favoriteService = new FavoriteService(favoriteRepository, sectionRepository, stationService, memberService);
    }

    @DisplayName("즐겨찾기 추가 함수는, 시작역과 종료역을 입력받아 즐겨찾기에 추가하고 추가된 즐겨찾기 정보를 반환한다.")
    @Test
    void createFavoriteTest() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());
        Favorite favorite = new Favorite(강남역, 양재역, member);

        when(stationService.lookUp(강남역.getId())).thenReturn(강남역);
        when(stationService.lookUp(양재역.getId())).thenReturn(양재역);
        when(favoriteRepository.save(favorite)).thenReturn(favorite);
        when(memberService.findMemberByEmail(member.getEmail())).thenReturn(member);
        when(sectionRepository.findAll()).thenReturn(연결되지_않은_두_구간);

        // when
        FavoriteResponse actual = favoriteService.createFavorite(member.getEmail(), favoriteRequest);

        // then
        assertThat(actual).isEqualTo(FavoriteResponse.from(favorite));
    }

    @DisplayName("즐겨찾기 추가 함수는, 입력된 역이 경로에 포함되지 않은 경우 NotAddedStationsToPathsException이 발생한다.")
    @Test
    void createFavoriteNotAddedStationsToSectionTest() {
        // given
        Station 포함되지_않은_역 = Station.of(-1L, "포함되지 않은 역");
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 포함되지_않은_역.getId());

        when(stationService.lookUp(강남역.getId())).thenReturn(강남역);
        when(stationService.lookUp(포함되지_않은_역.getId())).thenReturn(포함되지_않은_역);
        when(sectionRepository.findAll()).thenReturn(연결되지_않은_두_구간);

        // when
        ThrowingCallable actual = () -> favoriteService.createFavorite(member.getEmail(), favoriteRequest);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotAddedStationsToPathsException.class);
    }

    @DisplayName("즐겨찾기 추가 함수는, 연결되지 않은 역을 추가하려고 하는 경우 NotConnectedPathsException이 발생한다.")
    @Test
    void createFavoriteNotConnectedStationsTest() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 교대역.getId());

        when(stationService.lookUp(강남역.getId())).thenReturn(강남역);
        when(stationService.lookUp(교대역.getId())).thenReturn(교대역);
        when(sectionRepository.findAll()).thenReturn(연결되지_않은_두_구간);

        // when
        ThrowingCallable actual = () -> favoriteService.createFavorite(member.getEmail(), favoriteRequest);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotConnectedPathsException.class);
    }

    @DisplayName("즐겨찾기 조회 함수는, 주어진 회원의 즐겨찾기 목로을 반환한다.")
    @Test
    void findFavoritesTest() {
        // given
        Favorite favorite1 = new Favorite(1L, 강남역, 양재역, member);
        Favorite favorite2 = new Favorite(2L, 교대역, 홍대역, member);
        List<Favorite> favorites = List.of(favorite1, favorite2);

        when(memberService.findMemberByEmail(member.getEmail())).thenReturn(member);
        when(favoriteRepository.findByMemberId(member.getId())).thenReturn(favorites);

        // when
        List<FavoriteResponse> actual = favoriteService.findFavorites(member.getEmail());

        // then
        assertThat(actual).isEqualTo(createFavorites(favorites));
    }

    private static List<FavoriteResponse> createFavorites(List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    @DisplayName("즐겨찾기 삭제 함수는, favoriteRepository의 즐겨찾기 삭제 함수가 실행된다.")
    @Test
    void deleteFavoritesTest() {
        // given
        final Long favoriteId = 1L;

        when(memberService.findMemberByEmail(member.getEmail())).thenReturn(member);
        when(favoriteRepository.findByIdAndMemberId(favoriteId, member.getId()))
                .thenReturn(Optional.of(new Favorite(favoriteId, 강남역, 양재역, member)));

        // when
        favoriteService.deleteFavorite(favoriteId, member.getEmail());

        // then
        verify(favoriteRepository).deleteById(favoriteId);
    }

    @DisplayName("즐겨찾기 삭제 함수는, 존재하지 않는 즐겨찾기를 삭제하려 하면 NotExistFavoriteException이 발생한다.")
    @Test
    void deleteFavoriteNotExistFavoriteExceptionTest() {
        // given
        when(memberService.findMemberByEmail(member.getEmail())).thenReturn(member);
        when(favoriteRepository.findByIdAndMemberId(any(), any())).thenReturn(Optional.empty());

        // when
        ThrowingCallable actual = () -> favoriteService.deleteFavorite(1L, member.getEmail());

        // then
        assertThatThrownBy(actual).isInstanceOf(NotExistFavoriteException.class);
    }
}
