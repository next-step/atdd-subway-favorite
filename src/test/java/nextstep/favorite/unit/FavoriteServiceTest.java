package nextstep.favorite.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.utils.DatabaseCleanup;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class FavoriteServiceTest {

	@Autowired
	private FavoriteService favoriteService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private LineRepository lineRepository;

	@Autowired
	private DatabaseCleanup databaseCleanup;

	private Station sinsaStation;
	private Station nonhyeonStation;
	private Station newNonhyeonStation;
	private Member member;

	@BeforeEach
	void setUp() {
		databaseCleanup.execute();
		sinsaStation = stationRepository.save(new Station("신사역"));
		nonhyeonStation = stationRepository.save(new Station("논현역"));
		newNonhyeonStation = stationRepository.save(new Station("신논현역"));
		member = memberRepository.save(new Member("mandu@next.com", "test", 20));
		Line line = lineRepository.save(new Line("신분당선", "Red"));
		line.addSection(sinsaStation, nonhyeonStation, 10);
	}

	@Test
	void 즐겨찾기를_등록한다() {
		// given
		FavoriteRequest favoriteRequest = new FavoriteRequest(sinsaStation.getId(), nonhyeonStation.getId());

		// when
		Long savedId = favoriteService.create(favoriteRequest, member.getEmail());

		// then
		assertThat(savedId).isEqualTo(1L);
	}

	@Test
	void 즐겨찾기_등록_시_비정상_경로를_입력하면_에러를_반환한다() {
		// given
		FavoriteRequest favoriteRequest = new FavoriteRequest(nonhyeonStation.getId(), newNonhyeonStation.getId());

		// then
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> favoriteService.create(favoriteRequest, member.getEmail()));
	}

	@Test
	void 즐겨찾기_등록_시_등록되지_않은_역을_입력하면_에러를_반환한다() {
		// given
		Long notRegisterStation = 2L;
		FavoriteRequest favoriteRequest = new FavoriteRequest(notRegisterStation, newNonhyeonStation.getId());

		// then
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> favoriteService.create(favoriteRequest, member.getEmail()));
	}

	@Test
	void 즐겨찾기_등록_시_등록되지_않은_회원을_입력하면_에러를_반환한다() {
		// given
		String notRegisterMemberEmail = "notRegisterMemberEmail@next.com";
		FavoriteRequest favoriteRequest = new FavoriteRequest(nonhyeonStation.getId(), newNonhyeonStation.getId());

		// then
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> favoriteService.create(favoriteRequest,notRegisterMemberEmail));
	}
}
