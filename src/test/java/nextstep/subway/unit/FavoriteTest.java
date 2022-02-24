package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.UserDetails;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;

@SpringBootTest
public class FavoriteTest {
	@Autowired
	private FavoriteRepository favoriteRepository;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private MemberRepository memberRepository;

	@Test
	@Transactional
	void createFavoriteTest() {
		Station 강남역 = stationRepository.save(new Station("강남역"));
		Station 역삼역 = stationRepository.save(new Station("역삼역"));

		Member member = new Member("mj@naver.com", "1111", 10);
		memberRepository.save(member);

		FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
		FavoriteService favoriteService = new FavoriteService(memberRepository, stationRepository, favoriteRepository);

		UserDetails userDetails = new UserDetails(1L, "mj@naver.com", "1111", 10);

		favoriteService.createFavorites(userDetails, favoriteRequest);

		member = memberRepository.findById(1L).get();
		assertThat(member.getFavorites().getFavorites().get(0).getSource()).isEqualTo(강남역);
		assertThat(member.getFavorites().getFavorites().get(0).getTarget()).isEqualTo(역삼역);

	}

	@Test
	@Transactional
	void deleteFavoriteTest() {
		Station 강남역 = stationRepository.save(new Station("강남역"));
		Station 역삼역 = stationRepository.save(new Station("역삼역"));

		Member member = new Member("mj@naver.com", "1111", 10);
		memberRepository.save(member);

		FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
		FavoriteService favoriteService = new FavoriteService(memberRepository, stationRepository, favoriteRepository);

		UserDetails userDetails = new UserDetails(1L, "mj@naver.com", "1111", 10);

		favoriteService.createFavorites(userDetails, favoriteRequest);
		favoriteService.deleteFavorite(userDetails, 1L);
		member = memberRepository.findById(1L).get();
		assertThat(member.getFavorites().getFavorites().size()).isEqualTo(0);
	}
}
