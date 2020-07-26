package nextstep.subway.favorite.application;

import nextstep.subway.favorite.application.exceptions.AccessViolationException;
import nextstep.subway.favorite.application.exceptions.FavoriteNotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("즐겨찾기 서비스에 대한 유닛 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private MemberRepository memberRepository;

    private FavoriteService service;

    @BeforeEach
    void setUp() {
        service = new FavoriteService(favoriteRepository, stationRepository, memberRepository);
    }

    @DisplayName("멤버가 즐겨찾기를 추가하였다.")
    @Test
    void createFavorite() {

        // given
        final FavoriteRequest request = new FavoriteRequest(1L, 3L);
        final Long currentMemberId = 1L;

        // stubbing
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(new Member()));

        // when
        service.createFavorite(request, currentMemberId);

        // then
        verify(favoriteRepository).save(any(Favorite.class));
    }

    @DisplayName("소유자가 즐겨찾기를 삭제한다.")
    @Test
    void deleteFavoriteByOwner() {

        // stubbing
        final Member member = new Member("a@b.com", "1234", 88);
        ReflectionTestUtils.setField(member, "id", 1L);
        final Favorite favorite = new Favorite(1L, 3L, member);
        when(favoriteRepository.findById(anyLong())).thenReturn(Optional.of(favorite));
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        // when
        service.deleteFavorite(1L, 1L);

        // then
        verify(favoriteRepository).deleteById(anyLong());
    }

    @DisplayName("다른 사람이 내 즐겨찾기를 지우려고한다.")
    @Test
    void deleteFavoriteWithoutPermission() {

        // stubbing
        final Member owner = new Member("a@b.com", "1234", 88);
        ReflectionTestUtils.setField(owner, "id", 1L);
        final Favorite favorite = new Favorite(1L, 3L, owner);
        when(favoriteRepository.findById(anyLong())).thenReturn(Optional.of(favorite));
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(new Member()));

        // when
        assertThatThrownBy(() -> service.deleteFavorite(1L, 3L))
                .isInstanceOf(AccessViolationException.class);
    }

    @DisplayName("없는 즐겨찾기를 지우려고 한다.")
    @Test
    void deleteNotExistingFavorite() {
        assertThatThrownBy(() -> service.deleteFavorite(1L, 3L))
                .isInstanceOf(FavoriteNotFoundException.class);
    }
}