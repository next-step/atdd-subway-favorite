package nextstep.subway.unit;

import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.FavoriteNotOwningException;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("FavoriteService 단위 테스트")
@SpringBootTest
@Transactional
class FavoriteServiceTest {

	String EMAIL = "email@email.com";
	String PASSWORD = "password";
	Long 가양역_아이디;
	Long 강남역_아이디;
	Member 회원;
	Favorite 즐겨찾기;

	FavoriteService favoriteService;

	@Autowired
	FavoriteRepository favoriteRepository;

	@Autowired
	StationRepository stationRepository;

	@Autowired
	MemberRepository memberRepository;


	@BeforeEach
	void setUp() {
		favoriteService = new FavoriteService(favoriteRepository, stationRepository, memberRepository);

		Station 가양역 = new Station("가양역");
		Station 강남역 = new Station("강남역");
		회원 = new Member(EMAIL, PASSWORD, 10);
		memberRepository.save(회원);

		즐겨찾기 = new Favorite(가양역, 강남역, 회원);

		가양역_아이디 = stationRepository.save(가양역).getId();
		강남역_아이디 = stationRepository.save(강남역).getId();
	}

	@AfterEach
	void cleanUp() {
		favoriteRepository.deleteAll();
		memberRepository.deleteAll();
		stationRepository.deleteAll();
	}

	@DisplayName("FavoriteService - 조회")
	@Test
	void findByMemberId() {
		// given
		favoriteService.save(회원.getId(), getFavoriteRequest(가양역_아이디, 강남역_아이디));

		// when
		List<FavoriteResponse> favoriteResponses = favoriteService.findByMemberId(회원.getId());

		// then
		assertThat(favoriteResponses.size()).isEqualTo(1);
		assertThat(favoriteResponses.get(0))
				.extracting(FavoriteResponse::getSource)
				.extracting(StationResponse::getId)
				.isEqualTo(가양역_아이디);
		assertThat(favoriteResponses.get(0))
				.extracting(FavoriteResponse::getTarget)
				.extracting(StationResponse::getId)
				.isEqualTo(강남역_아이디);
	}

	@DisplayName("FavoriteService - 생성")
	@Test
	void save() {
		// when
		FavoriteResponse response = favoriteService.save(회원.getId(), getFavoriteRequest(가양역_아이디, 강남역_아이디));

		// then
		assertThat(response.getSource()).extracting(StationResponse::getId).isEqualTo(가양역_아이디);
		assertThat(response.getTarget()).extracting(StationResponse::getId).isEqualTo(강남역_아이디);
	}

	@DisplayName("FavoriteService - 제거")
	@Test
	void removeById() {
		// given
		FavoriteResponse favoriteResponse = favoriteService.save(회원.getId(), getFavoriteRequest(가양역_아이디, 강남역_아이디));

		// when
		long favoriteId = favoriteResponse.getId();
		favoriteService.removeById(favoriteId, 회원.getId());

		// then
		assertThat(favoriteRepository.findById(favoriteId)).isEmpty();
	}

	@DisplayName("삭제를 요청한 favorite 을 소유하지 않은 회원일 경우")
	@Test
	void favoriteNotOwningException() {
		// given
		FavoriteResponse favoriteResponse = favoriteService.save(회원.getId(), getFavoriteRequest(가양역_아이디, 강남역_아이디));

		Member 다른_회원 = new Member(EMAIL, PASSWORD, 10);
		memberRepository.save(다른_회원);

		// when
		assertThatThrownBy(() -> favoriteService.removeById(favoriteResponse.getId(), 다른_회원.getId()))
				.isInstanceOf(FavoriteNotOwningException.class);
	}

	private FavoriteRequest getFavoriteRequest(long source, long target) {
		return new FavoriteRequest(source, target);
	}
}