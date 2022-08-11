package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.member.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoritesResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceMockTest {

    private static final LoginMember LOGIN_MEMBER = new LoginMember("user@email.com", "password", List.of(RoleType.ROLE_MEMBER.name()));
    private static final Member MEMBER = new Member(1L, "user@email.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name()));
    private static final long 강남역_ID = 1L;
    private static final long 역삼역_ID = 2L;
    private static final Station 강남역 = new Station(강남역_ID, "강남역");
    private static final Station 역삼역 = new Station(역삼역_ID, "역삼역");
    @Mock
    FavoriteRepository favoriteRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    StationRepository stationRepository;

    @Autowired
    FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, memberRepository, stationRepository);
    }

    @Test
    @DisplayName("로그인한 사용자의 정보와 출발역, 그리고 도착역을 가져와 즐겨찾기를 등록합니다.")
    void saveFavorite() {
        // given
        FavoriteRequest 요청 = new FavoriteRequest(강남역_ID, 역삼역_ID);
        Favorite 즐겨찾기 = mock(Favorite.class);

        // when
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(MEMBER));
        when(stationRepository.findById(강남역_ID)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(역삼역_ID)).thenReturn(Optional.of(역삼역));
        when(favoriteRepository.save(any())).thenReturn(즐겨찾기);
        when(즐겨찾기.getId()).thenReturn(1L);
        when(즐겨찾기.getSource()).thenReturn(강남역);
        when(즐겨찾기.getTarget()).thenReturn(역삼역);

        // then
        assertDoesNotThrow(
            () -> favoriteService.saveFavorite(LOGIN_MEMBER, 요청)
        );
    }

    @Test
    @DisplayName("로그인한 사용자의 정보를 가져와 사용자의 즐겨찾기 목록을 조회합니다.")
    void findFavorites() {
        Favorite 즐겨찾기 = mock(Favorite.class);

        // when
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(MEMBER));
        when(favoriteRepository.findAllByMemberId(MEMBER.getId())).thenReturn(List.of(즐겨찾기));
        when(즐겨찾기.getId()).thenReturn(1L);
        when(즐겨찾기.getSource()).thenReturn(강남역);
        when(즐겨찾기.getTarget()).thenReturn(역삼역);


        // then
        FavoritesResponse favorites = favoriteService.findFavorites(LOGIN_MEMBER);
        assertThat(favorites.getFavorites()).hasSize(1);
    }

    @Test
    @DisplayName("로그인한 사용자의 정보와 즐겨찾기의 식별자를 가져와 즐겨찾기를 삭제합니다.")
    void deleteFavorite() {
        Favorite 즐겨찾기 = mock(Favorite.class);

        // when
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(MEMBER));
        when(favoriteRepository.findById(any())).thenReturn(Optional.of(즐겨찾기));
        doNothing().when(favoriteRepository).delete(즐겨찾기);
        when(즐겨찾기.getId()).thenReturn(1L);
        when(즐겨찾기.getMemberId()).thenReturn(1L);

        // then
        assertDoesNotThrow(
            () -> favoriteService.deleteFavorite(LOGIN_MEMBER, 즐겨찾기.getId())
        );
    }

    @Test
    @DisplayName("등록한 사용자가 아닌 경우 Unauthorized 예외가 발생합니다.")
    void deleteFavorite_invalid_memberId() {
        // given
        long 다른_회원_ID = 111L;
        long 즐겨찾기_ID = 1L;
        Favorite 다른_회원의_즐겨찾기 = mock(Favorite.class);

        // when
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(MEMBER));
        when(favoriteRepository.findById(any())).thenReturn(Optional.of(다른_회원의_즐겨찾기));
        when(다른_회원의_즐겨찾기.getMemberId()).thenReturn(다른_회원_ID);

        // then
        assertThatThrownBy(
            () -> favoriteService.deleteFavorite(LOGIN_MEMBER, 즐겨찾기_ID)
        ).isInstanceOf(AuthenticationException.class);
    }
}
