package nextstep.subway.unit.station.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.common.domain.model.exception.EntityNotFoundException;
import nextstep.station.application.StationService;
import nextstep.station.domain.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Station Service 테스트")
public class StationServiceTest {
    @Mock
    private StationRepository stationRepository;
    @InjectMocks
    private StationService stationService;

    @DisplayName("요청한 ID를 가지는 Entity가 없다면 Throw")
    @Test
    void verifyExists() {
        // Given
        when(stationRepository.existsById(anyLong()))
            .thenReturn(false);

        // When, Then
        assertThatThrownBy(() -> stationService.verifyExists(1L))
            .isInstanceOf(EntityNotFoundException.class);
    }
}
