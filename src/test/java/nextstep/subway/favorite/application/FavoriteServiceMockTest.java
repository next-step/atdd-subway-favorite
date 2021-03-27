package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceMockTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static int AGE = 30;

    @Mock
    private StationService stationService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Autowired
    private FavoriteService favoriteService;

    private Station 신림역;
    private Station 봉천역;
    private Station 사당역;
    private Station 서초역;
    private LoginMember 로그인;
    private FavoriteRequest 즐겨찾기_등록;
    private Favorite 즐겨찾기;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(stationService, favoriteRepository);
        신림역 = new Station("신림역");
        ReflectionTestUtils.setField(신림역, "id", 1L);
        봉천역 = new Station("봉천역");
        ReflectionTestUtils.setField(봉천역, "id", 2L);
        사당역 = new Station("사당역");
        ReflectionTestUtils.setField(사당역, "id", 3L);
        서초역 = new Station("서초역");
        ReflectionTestUtils.setField(서초역, "id", 4L);

        로그인 = new LoginMember(1L, EMAIL, PASSWORD, AGE);
        즐겨찾기_등록 = new FavoriteRequest(봉천역.getId(), 서초역.getId());

        즐겨찾기 = Favorite.of(로그인.getId(), 봉천역, 서초역);
        ReflectionTestUtils.setField(즐겨찾기, "id", 1L);
    }


    @Test
    void 즐겨찾기_등록() {
        when(favoriteRepository.save(any())).thenReturn(즐겨찾기);

        FavoriteResponse 즐겨찾기_결과 = favoriteService.save(로그인.getId(), 즐겨찾기_등록);

        assertThat(즐겨찾기_결과.getId()).isEqualTo(즐겨찾기.getId());
    }

    @Test
    void 즐겨찾기_목록_조회() {
        List<Favorite> favorites = new ArrayList<>();
        favorites.add(즐겨찾기);

        when(favoriteRepository.findAllByMemberId(로그인.getId())).thenReturn(favorites);

        List<FavoriteResponse> 즐겨찾기_리스트 = favoriteService.getAll(로그인.getId());

        assertThat(즐겨찾기_리스트.get(0).getId()).isEqualTo(즐겨찾기.getId());
    }

    @Test
    void 즐겨찾기_삭제() {
        when(favoriteRepository.save(any())).thenReturn(즐겨찾기);

        FavoriteResponse 즐겨찾기_결과 = favoriteService.save(로그인.getId(), 즐겨찾기_등록);

        favoriteService.delete(즐겨찾기_결과.getId(), 로그인);
    }
}
