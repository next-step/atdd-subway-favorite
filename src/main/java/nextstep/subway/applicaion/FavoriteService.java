package nextstep.subway.applicaion;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.PostFavoriteRequest;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Getter
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

	private final StationRepository stationRepository;
	private final MemberRepository memberRepository;
	private final FavoriteRepository favoriteRepository;

	@Transactional
	public Long save(String email, PostFavoriteRequest request) {
		Member member = findMemberWithEmail(email);

		Station source = stationRepository.findById(request.getSource())
				.orElseThrow(NoSuchElementException::new);
		Station target = stationRepository.findById(request.getTarget())
				.orElseThrow(NoSuchElementException::new);

		Favorite favorite = favoriteRepository.save(Favorite.of(member.getId(), source, target));

		return favorite.getId();
	}

	public List<FavoriteResponse> getFavorites(String email) {
		Member member = findMemberWithEmail(email);
		List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());
		return favorites.stream()
				.map(FavoriteResponse::of)
				.collect(Collectors.toList());
	}

	@Transactional
	public void deleteFavorite(Long id, String email) {
		Member member = findMemberWithEmail(email);
		Favorite favorite = favoriteRepository.findByIdAndMemberId(id, member.getId())
				.orElseThrow(NoSuchElementException::new);
		favoriteRepository.delete(favorite);
	}

	private Member findMemberWithEmail(String email) {
		return memberRepository.findByEmail(email)
				.orElseThrow(NoSuchElementException::new);
	}
}
