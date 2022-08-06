package nextstep.subway.applicaion;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.PostFavoriteRequest;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@Getter
@RequiredArgsConstructor
@Transactional
public class FavoriteService {

	private final StationRepository stationRepository;
	private final MemberRepository memberRepository;
	private final FavoriteRepository favoriteRepository;

	public Long save(String email, PostFavoriteRequest request) {
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(NoSuchElementException::new);

		Station source = stationRepository.findById(request.getSource())
				.orElseThrow(NoSuchElementException::new);
		Station target = stationRepository.findById(request.getTarget())
				.orElseThrow(NoSuchElementException::new);

		Favorite favorite = favoriteRepository.save(Favorite.of(member.getId(), source, target));

		return favorite.getId();
	}
}
