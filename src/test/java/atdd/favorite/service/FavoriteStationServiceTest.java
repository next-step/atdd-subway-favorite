package atdd.favorite.service;

import atdd.favorite.application.dto.CreateFavoriteStationRequestView;
import atdd.favorite.application.dto.FavoriteStationResponseView;
import atdd.favorite.domain.FavoriteStation;
import atdd.favorite.domain.FavoriteStationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FavoriteStationServiceTest {
    private static final String EMAIL = "abc@gmail.com";
    private static final Long stationId = 1L;
    private static final FavoriteStation favoriteStation
            = new FavoriteStation(1L, EMAIL, 1L);

    @InjectMocks
    private FavoriteStationService favoriteStationService;

    @Mock
    private FavoriteStationRepository favoriteStationRepository;

    @Test
    void 지하철역_즐겨찾기_등록이_된다() {
        //given
        CreateFavoriteStationRequestView requestView
                = new CreateFavoriteStationRequestView(EMAIL, stationId);
        given(favoriteStationRepository.save(any(FavoriteStation.class))).willReturn(favoriteStation);

        //when
        Optional<FavoriteStationResponseView> responseView = favoriteStationService.create(requestView);

        //then
        verify(favoriteStationRepository).save(any());
    }

    @Test
    void 같은_역을_여러_번_등록하면_안_된다() {
        //given
        CreateFavoriteStationRequestView requestView
                = new CreateFavoriteStationRequestView(EMAIL, stationId);
        given(favoriteStationRepository.save(any(FavoriteStation.class)))
                .willReturn(favoriteStation);
        given(favoriteStationRepository.findByStationId(stationId))
                .willReturn(Optional.of(favoriteStation));
        Optional<FavoriteStationResponseView> responseView1
                = favoriteStationService.create(requestView);

        //when
        Optional<FavoriteStationResponseView> responseView2 = favoriteStationService.create(requestView);

        //then
        verify(favoriteStationRepository, times(1)).save(any());
        assertThat(responseView1).isNotEmpty();
        assertThat(responseView2).isNull();
    }
}

