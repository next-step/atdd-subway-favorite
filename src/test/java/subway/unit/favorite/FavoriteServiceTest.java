package subway.unit.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.dto.favorite.FavoriteRequest;
import subway.favorite.Favorite;
import subway.favorite.FavoriteRepository;
import subway.favorite.FavoriteService;
import subway.member.Member;
import subway.member.MemberService;
import subway.station.Station;
import subway.station.StationService;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
	@InjectMocks
	private FavoriteService favoriteService;

	@Mock
	private StationService stationService;

	@Mock
	private MemberService memberService;

	@Mock
	private FavoriteRepository favoriteRepository;

	@DisplayName("즐겨 찾기를 저장한다.")
	@Test
	void save() {
		// given
		FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 3L);
		Member member = new Member("test@test.com", "password", 21);
		Station station = new Station("강남역");
		Station station1 = new Station("불광역");

		given(memberService.findMemberByEmail(anyString())).willReturn(member);
		given(stationService.findStationById(anyLong())).willReturn(station);
		given(stationService.findStationById(anyLong())).willReturn(station1);
		given(favoriteRepository.save(any(Favorite.class))).willReturn(new Favorite(1L, member, station, station1));

		// when
		Long save = favoriteService.save("test@test.com", favoriteRequest);

		// then
		assertThat(save).isEqualTo(1L);
	}
}
