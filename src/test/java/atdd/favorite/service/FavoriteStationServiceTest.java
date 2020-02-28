package atdd.favorite.service;

import atdd.favorite.application.dto.CreateFavoriteStationRequestView;
import atdd.favorite.domain.FavoriteStation;
import atdd.favorite.domain.FavoriteStationRepository;
import org.assertj.core.api.Assertions;
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
    void createFavoriteStation() {
        //given
        Long stationId = 1L;
        FavoriteStation favoriteStation = new FavoriteStation(1L, EMAIL, 1L);
        CreateFavoriteStationRequestView requestView
                = CreateFavoriteStationRequestView.builder()
                .email(EMAIL)
                .stationId(stationId)
                .build();
        given(favoriteStationRepository.save(any(FavoriteStation.class))).willReturn(favoriteStation);

        //when
        FavoriteStation savedFavoriteStation = favoriteStationService.create(requestView);

        //then
        verify(favoriteStationRepository).save(favoriteStation);
        assertThat(savedFavoriteStation.getStationId()).isEqualTo(stationId);
    }
}
