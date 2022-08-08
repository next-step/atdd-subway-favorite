package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.application.dto.FavoriteStationResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exception.NotOwnerFavoriteException;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceMockTest {

    private String EMAIL = "admin@next.com";

    @Mock
    private MemberService memberService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationService stationService;

    @InjectMocks
    private FavoriteService favoriteService;

    private Station 강남역;
    private Station 역삼역;
    private Station 삼성역;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        역삼역 = new Station("역삼역");
        ReflectionTestUtils.setField(역삼역, "id", 2L);
        삼성역 = new Station("삼성역");
        ReflectionTestUtils.setField(삼성역, "id", 3L);
    }

    @Test
    @DisplayName("즐겨찾기 추가하는 테스트입니다.")
    void saveFavorite() {
        when(stationService.findById(강남역.getId()))
            .thenReturn(강남역);
        when(stationService.findById(역삼역.getId()))
            .thenReturn(역삼역);
        when(memberService.findMember(EMAIL))
            .thenReturn(new MemberResponse(1L, EMAIL, 20));
        when(favoriteRepository.save(any()))
            .thenReturn(new Favorite(1L, 강남역.getId(), 역삼역.getId()));
        when(favoriteRepository.findById(any()))
            .thenReturn(Optional.of(new Favorite(1L, 강남역.getId(), 역삼역.getId())));

        FavoriteResponse response = favoriteService.saveFavorite(EMAIL, new FavoriteRequest(강남역.getId(), 역삼역.getId()));

        Favorite favorite = favoriteRepository.findById(response.getId()).get();

        assertThat(favorite).isEqualTo(new Favorite(response.getId(), 1L,  강남역.getId(), 역삼역.getId()));
    }

    @Test
    @DisplayName("즐겨찾기 목록을 조회합니다.")
    void findFavorites() {
        when(stationService.findById(강남역.getId()))
            .thenReturn(강남역);
        when(stationService.findById(역삼역.getId()))
            .thenReturn(역삼역);
        when(memberService.findMember(EMAIL))
            .thenReturn(new MemberResponse(1L, EMAIL, 20));
        when(favoriteRepository.findByMemberId(any()))
            .thenReturn(List.of(new Favorite(1L, 강남역.getId(), 역삼역.getId())));

        FavoriteResponse favoriteResponse = favoriteService.findFavorites(EMAIL).get(0);

        assertThat(favoriteResponse.getSource()).isEqualTo(new FavoriteStationResponse(강남역.getId(), 강남역.getName()));
        assertThat(favoriteResponse.getTarget()).isEqualTo(new FavoriteStationResponse(역삼역.getId(), 역삼역.getName()));
    }

    @Test
    @DisplayName("즐겨찾기 삭제 테스트")
    void delteFavorite() {
        when(favoriteRepository.findById(any()))
            .thenReturn(Optional.of(new Favorite(1L, 강남역.getId(), 역삼역.getId())));
        when(memberService.findMember(EMAIL))
            .thenReturn(new MemberResponse(1L, EMAIL, 20));

        favoriteService.deleteFavorite(EMAIL, 1L);
    }

    @Test
    @DisplayName("즐겨찾기 삭제할때 자신꺼가 아닐때 에러를 반환합니다.")
    void delteFavoriteException() {
        when(favoriteRepository.findById(any()))
            .thenReturn(Optional.of(new Favorite(2L, 강남역.getId(), 역삼역.getId())));
        when(memberService.findMember(EMAIL))
            .thenReturn(new MemberResponse(1L, EMAIL, 20));

        assertThatExceptionOfType(NotOwnerFavoriteException.class).isThrownBy(() -> {
            favoriteService.deleteFavorite(EMAIL, 1L);
        }).withMessage("자신의 즐겨찾기만 제거가 가능합니다.");
    }

}
