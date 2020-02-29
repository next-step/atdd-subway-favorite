package atdd.favorite.service;

import atdd.favorite.application.dto.FavoritePathRequestView;
import atdd.favorite.application.dto.FavoritePathResponseView;
import atdd.favorite.domain.FavoritePath;
import atdd.favorite.domain.FavoritePathRepository;
import atdd.path.application.GraphService;
import atdd.path.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static atdd.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FavoritePathServiceTest {
    private static final String EMAIL = "abc@gmail.com";
    private static final String EMAIL2 = "bbb@gmail.com";
    private static final Long stationId = 1L;
    private static final Long stationId2 = 2L;
    private static final Long stationId3 = 3L;
    private static final FavoritePath favoritePath
            = new FavoritePath(2L, EMAIL, stationId, stationId3);
    private static Station station = new Station(stationId, STATION_NAME);
    private static Station station2 = new Station(stationId, STATION_NAME_2);
    private static Station station3 = new Station(stationId, STATION_NAME_3);

    @InjectMocks
    FavoritePathService favoritePathService;

    @Mock
    FavoritePathRepository favoritePathRepository;

    @Mock
    GraphService graphService;

    @Test
    void 지하철경로를_즐겨찾기에_등록한다() throws Exception {
        //given
        FavoritePathRequestView requestView
                = new FavoritePathRequestView(EMAIL, stationId, stationId3);
        int theNumberOfStations = 3;
        given(graphService.findPath(stationId, stationId3))
                .willReturn(Arrays.asList(station, station2, station3));
        given(favoritePathRepository.save(any(FavoritePath.class)))
                .willReturn(favoritePath);

        //when
        FavoritePathResponseView responseView = favoritePathService.create(requestView);

        //then
        verify(favoritePathRepository, times(1))
                .save(any(FavoritePath.class));
        assertThat(responseView.getFavoritePathStations().size()).isEqualTo(theNumberOfStations);
    }

    @Test
    void 출발역과_도착역이_같으면_등록이_안_된다() throws Exception {
        //given
        FavoritePathRequestView requestView
                = new FavoritePathRequestView(EMAIL, stationId, stationId);
        FavoritePath favoritePathSame
                = new FavoritePath(EMAIL, stationId, stationId);

        //when, then
        assertThrows(IllegalArgumentException.class, () -> favoritePathService.create(requestView));
    }

    @Test
    void 지하철경로를_즐겨찾기에서_삭제한다() throws Exception{
        //given
        FavoritePathRequestView requestView
                = new FavoritePathRequestView(1L, EMAIL);
        FavoritePath favoritePath = new FavoritePath(1L, EMAIL, stationId, stationId3);

        //when
        favoritePathService.delete(requestView);

        //then
        verify(favoritePathRepository, times(1))
                .delete(any(FavoritePath.class));
    }

    @Test
    void 즐겨찾기를_등록한_사람만_삭제할_수_있다(){
        FavoritePathRequestView requestView
                = new FavoritePathRequestView(1L, EMAIL2);
        FavoritePath favoritePath = new FavoritePath(1L, EMAIL, stationId, stationId3);
        given(favoritePathRepository.findById(1L)).willReturn(Optional.of(favoritePath));

        //when, then
        assertThrows(IllegalArgumentException.class, () -> {
            favoritePathService.delete(requestView);
        });
    }
}
