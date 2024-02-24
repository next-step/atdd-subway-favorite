package subway.unit.favorite;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static subway.fixture.member.MemberEntityFixture.*;
import static subway.fixture.station.StationEntityFixture.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.dto.favorite.FavoriteRequest;
import subway.dto.favorite.FavoriteResponse;
import subway.favorite.Favorite;
import subway.favorite.FavoriteRepository;
import subway.favorite.FavoriteService;
import subway.fixture.favorite.FavoriteEntityFixture;
import subway.fixture.favorite.FavoriteRequestFixture;
import subway.member.Member;
import subway.member.MemberService;
import subway.path.PathService;
import subway.station.Station;
import subway.station.StationService;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceMockTest {
	private static final String EMAIL = "test@test.com";

	@InjectMocks
	private FavoriteService favoriteService;

	@Mock
	private StationService stationService;

	@Mock
	private MemberService memberService;

	@Mock
	private PathService pathService;

	@Mock
	private FavoriteRepository favoriteRepository;

	private FavoriteRequest favoriteRequest;
	private Member member;
	private Station 강남역;
	private Station 불광역;

	@BeforeEach
	void setUp() {
		favoriteRequest = FavoriteRequestFixture.builder().build();
		member = 멤버_생성();
		강남역 = 강남역();
		불광역 = 불광역();
	}

	@DisplayName("즐겨 찾기를 저장한다.")
	@Test
	void successSave() {
		// given
		given(memberService.findMemberByEmail(anyString())).willReturn(member);
		given(stationService.findStationById(anyLong())).willReturn(강남역).willReturn(불광역);
		given(favoriteRepository.save(any())).willReturn(FavoriteEntityFixture.즐겨찾기_생성(강남역, 불광역));

		// when
		FavoriteResponse actualResponse = favoriteService.save(EMAIL, favoriteRequest);

		// then
		FavoriteResponse expectedResponse = FavoriteResponse.of(member.getFavoriteList().get(0));
		assertThat(actualResponse)
			.usingRecursiveComparison()
			.isEqualTo(expectedResponse);
	}

	@DisplayName("출발과 도착이 같은 정류장을 즐겨 찾기 할 경우, 오류를 뱉어낸다.")
	@Test
	void failSaveSameStation() {
		// given
		given(memberService.findMemberByEmail(anyString())).willReturn(member);
		given(stationService.findStationById(anyLong())).willReturn(강남역).willReturn(강남역);

		// when
		// then
		assertThatThrownBy(() -> favoriteService.save(EMAIL, favoriteRequest))
			.hasMessage("출발역과 도착역은 동일할 수 없습니다.")
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("연결 되지 않은 두 정류장을 입력할 경우, 오류를 뱉어낸다.")
	@Test
	void failSaveNotConnectedStation() {
		// given
		given(memberService.findMemberByEmail(anyString())).willReturn(member);
		given(stationService.findStationById(anyLong())).willReturn(강남역).willReturn(불광역);
		willThrow(new IllegalArgumentException("경로를 찾을수 없습니다.")).given(pathService).findShortestPath(any(), any());

		// when
		// then
		assertThatThrownBy(() -> favoriteService.save(EMAIL, favoriteRequest))
			.hasMessage("경로를 찾을수 없습니다.")
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("즐겨찾기를 조회한다.")
	void successFindFavorite() {
		// given
		member.addFavorite(new Favorite(member, 강남역, 불광역));
		given(memberService.findMemberByEmail(anyString())).willReturn(member);

		// when
		List<FavoriteResponse> actualResponses = favoriteService.findFavorite(EMAIL);

		// then
		List<FavoriteResponse> expectedResponses = member.getFavoriteList()
			.stream()
			.map(FavoriteResponse::of)
			.collect(toList());
		assertThat(actualResponses).usingRecursiveComparison().isEqualTo(expectedResponses);
	}

	@DisplayName("즐겨찾기를 삭제한다.")
	@Test
	void successDeleteFavorite() {
		// given
		Favorite favorite = new Favorite(member, 강남역, 불광역);
		member.addFavorite(favorite);
		given(memberService.findMemberByEmail(anyString())).willReturn(member);
		given(favoriteRepository.findById(anyLong())).willReturn(Optional.of(favorite));

		// when
		favoriteService.delete(EMAIL, 1L);

		// then
		assertThat(member.getFavoriteList()).hasSize(0);
	}
}
