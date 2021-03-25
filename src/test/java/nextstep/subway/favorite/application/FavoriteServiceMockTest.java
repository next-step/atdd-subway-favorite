package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceMockTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationRepository stationRepository;

    @Autowired
    private FavoriteService favoriteService;
    private Station 강남역;
    private Station 역삼역;
    private LoginMember 로그인;
    private FavoriteRequest 즐겨찾기_신청;
    private Favorite 즐겨찾기;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, stationRepository);
        강남역 = new Station("강남역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        역삼역 = new Station("역삼역");
        ReflectionTestUtils.setField(역삼역, "id", 2L);
        로그인 = new LoginMember(1L, EMAIL, PASSWORD, AGE);
        즐겨찾기_신청 = new FavoriteRequest(강남역.getId(), 역삼역.getId());
        즐겨찾기 = new Favorite(로그인.getId(), 강남역, 역삼역);
    }

    @Test
    void createFavorite() {
        // given
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(역삼역.getId())).thenReturn(Optional.of(역삼역));
        when(favoriteRepository.save(any())).thenReturn(즐겨찾기);

        // when
        FavoriteResponse 즐겨찾기_결과 = favoriteService.saveFavorites(로그인, 즐겨찾기_신청);

        assertThat(즐겨찾기_결과).isNotNull();
        assertThat(즐겨찾기_결과.getId()).isEqualTo(즐겨찾기.getId());
    }
}
