package nextstep.subway.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceMockTest {

    @InjectMocks
    private FavoriteService favoriteService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationService stationService;

    @Test
    public void create_favorite_success() {
        // given
        Member member = new Member(1L, "email", "password", 20);
        Station 출발역 = new Station(2L, "출발역");
        Station 도착역 = new Station(3L, "도착역");
        Favorite favorite = new Favorite(4L, member.getId(), 출발역, 도착역);

        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
        given(stationService.findById(출발역.getId())).willReturn(출발역);
        given(stationService.findById(도착역.getId())).willReturn(도착역);
        given(favoriteRepository.save(any())).willReturn(favorite);

        // when
        Long findFavorite = favoriteService.createFavorite(member.getEmail(), 출발역.getId(), 도착역.getId());

        // then
        assertThat(findFavorite).isEqualTo(4L);
    }
}
