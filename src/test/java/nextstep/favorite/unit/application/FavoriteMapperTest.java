package nextstep.favorite.unit.application;

import static nextstep.Fixtures.*;
import static org.mockito.BDDMockito.given;

import nextstep.favorite.application.FavoriteMapper;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.subway.station.application.StationReader;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("FavoriteMapper 단위 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteMapperTest {
  @Mock private StationReader stationReader;
  @InjectMocks private FavoriteMapper favoriteMapper;

  @DisplayName("엔티티를 DTO 로 변환한다.")
  @Test
  void mapToResponse() {
    Favorite favorite = aFavorite().build();
    Station 교대역 = 교대역();
    Station 양재역 = 양재역();
    given(stationReader.readById(1L)).willReturn(교대역);
    given(stationReader.readById(2L)).willReturn(양재역);

    FavoriteResponse actualResponse = favoriteMapper.mapToFavoriteResponse(favorite);

    Assertions.assertThat(actualResponse).isEqualTo(FavoriteResponse.of(1L, 교대역, 양재역));
  }
}
