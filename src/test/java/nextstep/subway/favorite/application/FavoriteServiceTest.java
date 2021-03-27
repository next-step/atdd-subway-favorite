package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@SpringBootTest
@Transactional
class FavoriteServiceTest {

	private static final long MEMBER_ID = 1L;

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private FavoriteService favoriteService;

	private Station 강남역;
	private Station 양재역;

	@BeforeEach
	void setUp() {
		강남역 = stationRepository.save(new Station("강남역"));
		양재역 = stationRepository.save(new Station("양재역"));
	}

	@DisplayName("즐겨찾기 생성")
	@Test
	void createFavorites() {
		// when
		Long favoriteId = favoriteService.createFavorite(MEMBER_ID, new FavoriteRequest(강남역.getId(), 양재역.getId()));

		// then
		assertThat(favoriteId).isNotNull();
	}
}