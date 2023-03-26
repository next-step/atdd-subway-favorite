package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exception.CannotDeleteFavorite;
import nextstep.favorite.exception.FavoriteErrorMessage;
import nextstep.favorite.exception.NotFoundException;
import nextstep.member.application.dto.AuthUser;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;

@Transactional
class FavoriteServiceTest extends SpringBootServiceTest {
	public static final Long 존재하지_않는_즐겨찾기 = Long.MAX_VALUE;

	@Autowired
	FavoriteService favoriteService;
	@Autowired
	FavoriteRepository favoriteRepository;
	@Autowired
	StationRepository stationRepository;
	@Autowired
	LineRepository lineRepository;
	@Autowired
	MemberRepository memberRepository;

	AuthUser 로그인한_사용자;
	Member 사용자;
	AuthUser 로그인한_다른_사용자;
	Member 다른_사용자;
	Station 강남역;
	Station 양재역;
	Line 신분당선;

	@BeforeEach
	public void setUp() {
		super.setUp();
		// Given 지하철 역 2개와 사용자가 주어지고
		강남역 = stationRepository.save(new Station("강남역"));
		양재역 = stationRepository.save(new Station("양재역"));
		신분당선 = lineRepository.save(createLine(강남역, 양재역));

		사용자 = memberRepository.save(
			new Member("member@email.com", "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));
		로그인한_사용자 = AuthUser.of(사용자.getEmail(), 사용자.getRoles());
		다른_사용자 = memberRepository.save(
			new Member("otherMember@email.com", "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));
		로그인한_다른_사용자 = AuthUser.of(다른_사용자.getEmail(), 다른_사용자.getRoles());
	}

	@DisplayName("로그인 사용자의 즐겨찾기 저장")
	@Test
	void saveFavorite() {
		// When
		favoriteService.saveFavorite(로그인한_사용자, new FavoriteRequest(강남역.getId(), 양재역.getId()));

		// Then
		List<Favorite> favorites = favoriteRepository.findAll();

		assertThat(favorites.stream()
			.anyMatch(favorite -> Objects.equals(favorite.getSource(), 강남역) &&
				Objects.equals(favorite.getTarget(), 양재역) &&
				Objects.equals(favorite.getMember(), 사용자)))
			.isTrue();
	}

	@DisplayName("로그인 사용자의 즐겨찾기 목록 조회")
	@Test
	void findAllFavorites() {
		// Given
		FavoriteResponse favorite1 = favoriteService.saveFavorite(로그인한_사용자,
			new FavoriteRequest(강남역.getId(), 양재역.getId()));
		FavoriteResponse favorite2 = favoriteService.saveFavorite(로그인한_사용자,
			new FavoriteRequest(양재역.getId(), 강남역.getId()));

		// When
		List<Long> favoriteIds = favoriteService.findAllFavorites(로그인한_사용자)
			.stream()
			.map(FavoriteResponse::getId)
			.collect(
				Collectors.toList());

		// Then
		assertThat(favoriteIds).containsOnly(favorite1.getId(), favorite2.getId());
	}

	@DisplayName("로그인 사용자의 즐겨찾기 삭제")
	@Test
	void deleteFavoriteById() {
		// Given
		FavoriteResponse favorite = favoriteService.saveFavorite(로그인한_사용자,
			new FavoriteRequest(강남역.getId(), 양재역.getId()));

		// When
		favoriteService.deleteFavoriteById(로그인한_사용자, favorite.getId());

		// Then
		List<FavoriteResponse> favorites = favoriteService.findAllFavorites(로그인한_사용자);
		assertThat(favorites).doesNotContain(favorite);
	}

	@DisplayName("로그인 사용자의 즐겨찾기 삭제 - 예외 : 다른 사람의 즐겨찾기 삭제 시도")
	@Test
	void deleteFavoriteById_fail_ATTEMPTING_TO_DELETE_FAVORITE_OF_OTHER_USER() {
		// Given
		FavoriteResponse favorite = favoriteService.saveFavorite(로그인한_사용자,
			new FavoriteRequest(강남역.getId(), 양재역.getId()));

		// When & Then
		assertThatThrownBy(
			() -> favoriteService.deleteFavoriteById(로그인한_다른_사용자, favorite.getId()))
			.isInstanceOf(CannotDeleteFavorite.class)
			.hasMessage(FavoriteErrorMessage.SHOULD_BE_OWN_FAVORITES.getMessage());
	}

	@DisplayName("로그인 사용자의 즐겨찾기 삭제 - 예외 : 존재하지 않는 즐겨찾기 삭제 시도")
	@Test
	void deleteFavoriteById_fail_ATTEMPTING_TO_DELETE_NOT_EXISTED_FAVORITE() {
		// When & Then
		assertThatThrownBy(
			() -> favoriteService.deleteFavoriteById(로그인한_다른_사용자, 존재하지_않는_즐겨찾기))
			.isInstanceOf(NotFoundException.class)
			.hasMessage(FavoriteErrorMessage.NOT_FOUND_FAVORITE.getMessage());
	}

	private Line createLine(Station 강남역, Station 역삼역) {
		Line line = new Line("2호선", "green");
		line.addSection(강남역, 역삼역, 10);
		return line;
	}
}
