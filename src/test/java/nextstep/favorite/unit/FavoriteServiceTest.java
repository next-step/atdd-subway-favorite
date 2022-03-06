package nextstep.favorite.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private FavoriteService favoriteService;

	private Station 홍대입구역;
	private Station 합정역;
	private Station 당산역;
	private Member 회원;

	/**
	 * Given 지하철역, 회원 등록되어 있음.
	 */
	@BeforeEach
	void setUp() {
		홍대입구역 = stationRepository.save(new Station("홍대입구역"));
		합정역 = stationRepository.save(new Station("합정역"));
		당산역 = stationRepository.save(new Station("당산역"));

		회원 = memberRepository.save(new Member("email@email.com", "password", 30));
	}

	/**
	 * When 즐겨찾기 생성을 요청
	 * Then 즐겨찾기 생성됨
	 */
	@DisplayName("즐겨찾기 생성에 성공한다.")
	@Test
	void addFavorite() {
		FavoriteResponse response = 즐겨찾기_생성(회원.getId(), 홍대입구역.getId(), 당산역.getId());

		assertThat(response).isNotNull();
		assertThat(response.getSource().getId()).isEqualTo(홍대입구역.getId());
		assertThat(response.getTarget().getId()).isEqualTo(당산역.getId());
	}

	/**
	 * When 중복된 즐겨찾기 생성을 요청
	 * Then 즐겨찾기 생성 실패됨
	 */
	@DisplayName("이미 등록된 즐겨찾기와 동일한 데이터를 등록하면 실패한다.")
	@Test
	void addDuplicateFavorite() {
		즐겨찾기_생성(회원.getId(), 홍대입구역.getId(), 당산역.getId());

		FavoriteRequest duplicateRequest = new FavoriteRequest(홍대입구역.getId(), 당산역.getId());
		assertThatThrownBy(() -> favoriteService.addFavorite(회원.getId(), duplicateRequest))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("즐겨찾기가 등록되어 있습니다.");
	}

	/**
	 * When 즐겨찾기 리스트를 조회
	 * Then 즐겨찾기 리스트가 조회됨
	 */
	@DisplayName("등록된 즐겨찾기 리스트를 조회 성공한다.")
	@Test
	void getFavorites() {
		즐겨찾기_생성(회원.getId(), 홍대입구역.getId(), 당산역.getId());
		즐겨찾기_생성(회원.getId(), 당산역.getId(), 홍대입구역.getId());

		List<FavoriteResponse> response = favoriteService.findFavorites();
		assertThat(response)
				.extracting(FavoriteResponse::getSource)
				.map(stationResponse -> stationResponse.getId())
				.containsExactly(홍대입구역.getId(), 당산역.getId());
	}

	/**
	 * When 즐겨찾기 리스트를 삭제
	 * Then 즐겨찾기 리스트가 삭제됨
	 */
	@DisplayName("등록된 즐겨찾기중 하나를 삭제 성공한다.")
	@Test
	void deleteFavorites() {
		// given
		Long favoriteId = 즐겨찾기_생성(회원.getId(), 홍대입구역.getId(), 당산역.getId()).getId();

		// when
		favoriteService.deleteFavorite(회원.getId(), favoriteId);

		//then
		List<FavoriteResponse> response = favoriteService.findFavorites();
		assertThat(response).hasSize(0);
	}

	private FavoriteResponse 즐겨찾기_생성(Long memberId, Long sourceId, Long targetId) {
		FavoriteRequest request = new FavoriteRequest(sourceId, targetId);
		return favoriteService.addFavorite(memberId, request);
	}
}
