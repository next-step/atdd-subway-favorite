package nextstep.subway.unit;

import nextstep.subway.domain.entity.Favorite;
import nextstep.subway.dto.FavoriteRequest;
import nextstep.subway.dto.FavoriteResponse;
import nextstep.subway.dto.PathResponse;
import nextstep.subway.dto.StationResponse;
import nextstep.subway.repository.FavoriteRepository;
import nextstep.subway.service.FavoriteService;
import nextstep.subway.service.PathService;
import nextstep.subway.service.StationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {
	@Mock
	private FavoriteRepository favoriteRepository;
	@Mock
	private StationService stationService;
	@Mock
	private PathService pathService;
	private FavoriteService favoriteService;

	private Long source = 1L;
	private Long target = 2L;

	@BeforeEach
	void setup() {
		favoriteService = new FavoriteService(favoriteRepository, stationService, pathService);
	}

	@Test
	@DisplayName("즐겨찾기를 등록한다.")
	void saveFavorite() {
		// given
		StationResponse sourceResponse = new StationResponse(source, "source");
		StationResponse targetResponse = new StationResponse(target, "target");

		given(stationService.findStationById(source))
				.willReturn(sourceResponse);
		given(stationService.findStationById(target))
				.willReturn(targetResponse);
		given(pathService.getPath(source, target))
				.willReturn(new PathResponse(List.of(sourceResponse, targetResponse), 10));
		given(favoriteRepository.save(new Favorite(source, target)))
				.willReturn(new Favorite(source, target));

		// when
		FavoriteResponse response = favoriteService.saveFavorite(new FavoriteRequest(source, target));

		// then
		assertThat(response.getSource().getId()).isEqualTo(source);
		assertThat(response.getTarget().getId()).isEqualTo(target);
	}

	@Test
	@DisplayName("즐겨찾기를 삭제한다.")
	void deleteFavorites() {
		// given
		given(favoriteRepository.findById(1L))
				.willReturn(Optional.of(new Favorite(source, target)));

		// when
		assertDoesNotThrow(() -> favoriteService.deleteFavorite(1L));
	}

	@Test
	@DisplayName("존재하지 않는 즐겨찾기 삭제 시 실패한다.")
	void deleteFavoritesNotExist() {
		// given
		given(favoriteRepository.findById(1L))
				.willReturn(Optional.empty());

		// when
		assertThrows(EntityNotFoundException.class, () -> favoriteService.deleteFavorite(1L));
	}
}
