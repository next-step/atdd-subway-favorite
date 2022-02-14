package nextstep.subway.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
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

    private static final Long MEMBER_ID = 1L;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationService stationService;

    @Autowired
    private FavoriteService favoriteService;

    private Station 강남역, 역삼역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        ReflectionTestUtils.setField(역삼역, "id", 2L);
        when(stationService.findById(강남역.getId())).thenReturn(강남역);
        when(stationService.findById(역삼역.getId())).thenReturn(역삼역);

        List<Favorite> favoriteList = new ArrayList<>();
        favoriteList.add(new Favorite(MEMBER_ID, 강남역, 역삼역));
        when(favoriteRepository.findAllByMemberId(any())).thenReturn(favoriteList);
        favoriteService = new FavoriteService(favoriteRepository, stationService);
    }
    
    @Test
    void addFavorite() {
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 강남역.getId(), 역삼역.getId());
        favoriteService.createFavorite(MEMBER_ID, favoriteRequest);

        List<FavoriteResponse> responseList = favoriteService.findFavorite(MEMBER_ID);
        assertThat(responseList.size()).isEqualTo(1);
        assertThat(responseList.get(0).getSource().getName()).isEqualTo("강남역");
        assertThat(responseList.get(0).getTarget().getName()).isEqualTo("역삼역");
    }

}
