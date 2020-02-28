package atdd.favorite.service;

import atdd.favorite.application.dto.CreateFavoriteStationRequestView;
import atdd.favorite.domain.FavoriteStation;
import atdd.favorite.domain.FavoriteStationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FavoriteStationServiceTest {
    private static final String EMAIL = "abc@gmail.com";

    @InjectMocks
    private FavoriteStationService favoriteStationService;

    @Mock
    private FavoriteStationRepository favoriteStationRepository;

    @Test
    void 지하철역_즐겨찾기_등록이_된다() {
        //given
        Long stationId = 1L;
        FavoriteStation favoriteStation = new FavoriteStation(1L, EMAIL, 1L);
        CreateFavoriteStationRequestView requestView
                = new CreateFavoriteStationRequestView(EMAIL, stationId);
        given(favoriteStationRepository.save(any(FavoriteStation.class))).willReturn(favoriteStation);

        //when
        FavoriteStation favoriteStation1 = favoriteStationService.create(requestView);

        //then
        verify(favoriteStationRepository).save(any());
        assertThat(favoriteStation1.getEmail()).isEqualTo(EMAIL);
        assertThat(favoriteStation1.getStationId()).isEqualTo(stationId);
    }
}

