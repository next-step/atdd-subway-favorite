package nextstep.favorite.acceptance;

import nextstep.favorite.application.exceptions.CannotFavoriteStationException;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.dao.StationDao;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.auth.presentation.dto.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.path.service.PathService;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceMockTest {

    private FavoriteService favoriteService;
    @Mock
    FavoriteRepository favoriteRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    private PathService pathService;
    @Mock
    private StationDao stationDao;


    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, memberRepository, stationDao, pathService);
    }

    @Test
    @DisplayName("즐겨찾기 하는 두 경로가 연결되어 있지 않으면 즐겨찾기할 수 없다")
    public void shouldFailIfStationsAreNotNeighbor() {

        FavoriteRequest request = new FavoriteRequest(1L, 100L);
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(new Member("email", "pw", 12)));
        when(stationDao.findStation(anyLong())).thenReturn(new Station("강"));
        when(pathService.isConnectedPath(any(), any())).thenReturn(false);

        // when & then
        Assertions.assertThrows(CannotFavoriteStationException.class,
                () -> favoriteService.createFavorite(new LoginMember("email"), request));
    }

}
