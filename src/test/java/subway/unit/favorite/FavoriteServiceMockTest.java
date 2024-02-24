package subway.unit.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static subway.fixture.member.MemberEntityFixture.*;
import static subway.fixture.station.StationEntityFixture.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.dto.favorite.FavoriteRequest;
import subway.favorite.FavoriteService;
import subway.fixture.favorite.FavoriteRequestFixture;
import subway.member.Member;
import subway.member.MemberService;
import subway.path.PathService;
import subway.station.Station;
import subway.station.StationService;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceMockTest {
	@InjectMocks
	private FavoriteService favoriteService;

	@Mock
	private StationService stationService;

	@Mock
	private MemberService memberService;

	@Mock
	private PathService pathService;

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

		// when
		favoriteService.save("test@test.com", favoriteRequest);

		// then
		assertThat(member.getFavoriteList()).hasSize(1);
	}

	@DisplayName("출발과 도착이 같은 정류장을 즐겨 찾기 할 경우, 오류를 뱉어낸다.")
	@Test
	void failSaveSameStation() {
		// given
		given(memberService.findMemberByEmail(anyString())).willReturn(member);
		given(stationService.findStationById(anyLong())).willReturn(강남역).willReturn(강남역);

		// when
		// then
		assertThatThrownBy(() -> favoriteService.save("test@test.com", favoriteRequest))
			.hasMessage("출발역과 도착역은 동일할 수 없습니다.")
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("연결 되지 않은 두 정류장을 입력할 경우, 오류를 뱉어낸다.")
	@Test
	void failSaveNotConnectedStation() {
		// given
		given(memberService.findMemberByEmail(anyString())).willReturn(member);
		given(stationService.findStationById(anyLong())).willReturn(강남역).willReturn(강남역);
		willThrow(new IllegalArgumentException("경로를 찾을수 없습니다.")).given(pathService).findShortestPath(any(), any());

		// when
		// then
		assertThatThrownBy(() -> favoriteService.save("test@test.com", favoriteRequest))
			.hasMessage("경로를 찾을수 없습니다.")
			.isInstanceOf(IllegalArgumentException.class);
	}
}
