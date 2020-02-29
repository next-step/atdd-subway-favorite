package atdd.favorite.service;

import atdd.favorite.application.dto.FavoriteStationListResponseVIew;
import atdd.favorite.application.dto.FavoriteStationRequestView;
import atdd.favorite.application.dto.FavoriteStationResponseView;
import atdd.favorite.domain.FavoriteStation;
import atdd.favorite.domain.FavoriteStationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteStationServiceTest {
    private static final String EMAIL = "abc@gmail.com";
    private static final String EMAIL2 = "bbb@gmail.com";
    private static final Long stationId = 1L;
    private static final Long stationId2 = 2L;
    private static final FavoriteStation favoriteStation
            = new FavoriteStation(1L, EMAIL, stationId);
    private static final FavoriteStation favoriteStation2
            = new FavoriteStation(2L, EMAIL, stationId2);

    @InjectMocks
    private FavoriteStationService favoriteStationService;

    @Mock
    private FavoriteStationRepository favoriteStationRepository;

    @Test
    void 지하철역_즐겨찾기_등록이_된다() {
        //given
        FavoriteStationRequestView requestView
                = new FavoriteStationRequestView(EMAIL, stationId);
        given(favoriteStationRepository.save(any(FavoriteStation.class))).willReturn(favoriteStation);

        //when
        favoriteStationService.create(requestView);

        //then
        verify(favoriteStationRepository).save(any());
    }

    @Test
    void 같은_역을_여러_번_등록하면_안_된다() {
        //given
        FavoriteStationRequestView requestView
                = new FavoriteStationRequestView(EMAIL, stationId);
        given(favoriteStationRepository.save(any(FavoriteStation.class))).willReturn(favoriteStation);
        FavoriteStationResponseView responseView1 = favoriteStationService.create(requestView);
        given(favoriteStationRepository.findByStationId(stationId)).willReturn(Optional.of(favoriteStation));

        //when
        FavoriteStationResponseView responseView2 = favoriteStationService.create(requestView);

        //then
        verify(favoriteStationRepository, times(1)).save(any());
        assertThat(responseView1).isNotNull();
        assertThat(responseView2).isNull();
    }

    @Test
    void 지하철역_즐겨찾기를_삭제한다() throws Exception {
        //given
        given(favoriteStationRepository.findById(any(Long.class)))
                .willReturn(Optional.of(favoriteStation));

        //when
        favoriteStationService.delete(FavoriteStationRequestView.of(favoriteStation));

        //then
        verify(favoriteStationRepository, times(1))
                .delete(any(FavoriteStation.class));
    }

    @Test
    void 즐겨찾기_삭제는_등록한_사람만_가능하다() throws Exception {
        //given
        given(favoriteStationRepository.findById(any()))
                .willReturn(Optional.of(favoriteStation));
        FavoriteStationRequestView requestView = new FavoriteStationRequestView(EMAIL2, stationId);

        //when
        favoriteStationService.delete(requestView);

        //then
        verify(favoriteStationRepository, times(0))
                .delete(any(FavoriteStation.class));

    }

    @Test
    void 지하철역_즐겨찾기_목록을_불러온다() {
        //given
        List<FavoriteStation> favoriteStations = Arrays.asList(favoriteStation, favoriteStation2);
        given(favoriteStationRepository.findAllByEmail(EMAIL)).willReturn(favoriteStations);

        //when
        FavoriteStationListResponseVIew responseVIew
                = favoriteStationService.showAllFavoriteStations(EMAIL);

        //then
        verify(favoriteStationRepository, times(1))
                .findAllByEmail(anyString());
        assertThat(responseVIew.getFavoriteStations().size()).isEqualTo(favoriteStations.size());
    }
}

