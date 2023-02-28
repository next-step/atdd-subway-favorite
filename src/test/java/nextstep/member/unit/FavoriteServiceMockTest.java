package nextstep.member.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static nextstep.common.constants.ErrorConstant.NOT_FOUND_STATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceMockTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @InjectMocks
    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private StationService stationService;

    private Member 관리자;

    private Station 강남역;
    private Station 역삼역;

    @BeforeEach
    void setUp() {
        관리자 = new Member(EMAIL, PASSWORD, 20);

        강남역 = new Station("강남역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        역삼역 = new Station("역삼역");
        ReflectionTestUtils.setField(역삼역, "id", 2L);
    }

    @Test
    @DisplayName("경로 즐겨찾기 등록 실패-등록되지 않은 지하철역")
    void createFavorite_notEnrollStation() {
        // given
        when(memberService.findByEmail(EMAIL)).thenReturn(관리자);
        when(stationService.findById(강남역.getId())).thenThrow(IllegalArgumentException.class);

        // when
        // then
        assertThatThrownBy(() -> favoriteService.saveFavorite(EMAIL, new FavoriteRequest(강남역.getId(), 역삼역.getId())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_FOUND_STATION);
    }

    @Test
    @DisplayName("경로 즐겨찾기 등록")
    void createFavorite() {
        // given
        final Favorite returnValue = new Favorite(관리자, 강남역, 역삼역);
        ReflectionTestUtils.setField(returnValue, "id", 1L);

        when(memberService.findByEmail(EMAIL)).thenReturn(관리자);
        when(stationService.findById(강남역.getId())).thenReturn(강남역);
        when(stationService.findById(역삼역.getId())).thenReturn(역삼역);
        when(favoriteRepository.save(new Favorite(관리자, 강남역, 역삼역))).thenReturn(returnValue);

        // when
        final Favorite favorite = favoriteService.saveFavorite(EMAIL, new FavoriteRequest(강남역.getId(), 역삼역.getId()));

        // then
        assertThat(favorite.getId()).isEqualTo(1L);
        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }

    @Test
    @DisplayName("즐겨찾기 조회")
    void showFavorites() {
        // given
        final Favorite favorite = new Favorite(관리자, 강남역, 역삼역);
        ReflectionTestUtils.setField(favorite, "id", 1L);

        when(memberService.findByEmail(EMAIL)).thenReturn(관리자);
        when(favoriteRepository.findByMember(관리자)).thenReturn(List.of(favorite));

        // when
        final List<FavoriteResponse> favorites = favoriteService.showFavorites(EMAIL);

        // then
        assertThat(favorites.get(0).getId()).isEqualTo(1L);
        assertThat(favorites.get(0).getSource().getId()).isEqualTo(1L);
        assertThat(favorites.get(0).getTarget().getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("즐겨찾기 삭제")
    void deleteFavorite() {
        // when
        favoriteService.deleteFavorite(-1L);

        // then
        verify(favoriteRepository, times(1)).deleteById(-1L);
    }
}
