package nextstep.subway.application;

import nextstep.member.application.service.MemberService;
import nextstep.member.domain.entity.Member;
import nextstep.subway.domain.entity.Favorite;
import nextstep.subway.application.dto.FavoriteRequest;
import nextstep.subway.application.dto.FavoriteResponse;
import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.application.dto.StationResponse;
import nextstep.subway.domain.repository.FavoriteRepository;
import nextstep.subway.application.service.FavoriteService;
import nextstep.subway.application.service.PathService;
import nextstep.subway.application.service.StationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static nextstep.common.ReflectionUtils.setId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceMockTest {
	@Mock
	private FavoriteRepository favoriteRepository;
	@Mock
	private StationService stationService;
	@Mock
	private PathService pathService;
	@Mock
	private MemberService memberService;
	private FavoriteService favoriteService;

	private final Long source = 1L;
	private final Long target = 2L;
	private final String email = "aab555586@gmail.com";
	private final String password = "password";
	private final Member member = new Member(email, password, 20);
	private final Long memberId = 1L;
	private final Favorite favorite = new Favorite(memberId, source, target);
	private final Long favoriteId = 1L;

	@BeforeEach
	void setup() {
		favoriteService = new FavoriteService(favoriteRepository, stationService, pathService, memberService);

		setId(member, memberId);
		setId(favorite, favoriteId);

		given(memberService.findMemberByEmail(email))
				.willReturn(member);
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
		given(favoriteRepository.save(new Favorite(memberId, source, target)))
				.willReturn(favorite);

		// when
		FavoriteResponse response = favoriteService.saveFavorite(email, new FavoriteRequest(source, target));

		// then
		assertThat(response.getSource().getId()).isEqualTo(source);
		assertThat(response.getTarget().getId()).isEqualTo(target);
	}

	@Test
	@DisplayName("즐겨찾기를 삭제한다.")
	void deleteFavorites() {
		// given
		given(favoriteRepository.findByMemberId(memberId))
				.willReturn(List.of(favorite));

		// when
		assertDoesNotThrow(() -> favoriteService.deleteFavorite(email, favoriteId));
	}

	@Test
	@DisplayName("존재하지 않는 즐겨찾기 삭제 시 실패한다.")
	void deleteFavoritesNotExist() {
		// given
		given(favoriteRepository.findByMemberId(memberId))
				.willReturn(List.of());

		// when
		assertThrows(EntityNotFoundException.class, () -> favoriteService.deleteFavorite(email, favoriteId));
	}
}
