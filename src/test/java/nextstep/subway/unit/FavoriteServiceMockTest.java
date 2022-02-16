package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceMockTest {
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationService stationService;
    @Mock
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteService favoriteService;

    private Station 강남역;
    private Station 역삼역;
    private Member 사용자;
    private Favorite 즐겨찾기;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, stationService, memberRepository);
        강남역 = new Station("강남역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        역삼역 = new Station("역삼역");
        ReflectionTestUtils.setField(역삼역, "id", 2L);
        사용자 = new Member("email@email.com", "password", 30);
        ReflectionTestUtils.setField(사용자, "id", 1L);
        즐겨찾기 = new Favorite(강남역, 역삼역, 사용자);
        ReflectionTestUtils.setField(즐겨찾기, "id", 1L);
    }

    @Test
    void createFavorite() {
        when(stationService.findById(강남역.getId())).thenReturn(강남역);
        when(stationService.findById(역삼역.getId())).thenReturn(역삼역);
        when(memberRepository.findById(사용자.getId())).thenReturn(Optional.of(사용자));
        when(favoriteRepository.save(Mockito.any(Favorite.class))).thenReturn(즐겨찾기);

        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 역삼역.getId());
        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(favoriteRequest, 사용자.getId());

        assertThat(favoriteResponse.getId()).isEqualTo(1);
        assertThat(favoriteResponse.getSource().getName()).isEqualTo(StationResponse.of(강남역).getName());
        assertThat(favoriteResponse.getTarget().getName()).isEqualTo(StationResponse.of(역삼역).getName());
    }
}
